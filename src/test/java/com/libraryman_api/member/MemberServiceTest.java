package com.libraryman_api.member;

import com.libraryman_api.TestUtil;
import com.libraryman_api.exception.InvalidPasswordException;
import com.libraryman_api.exception.InvalidSortFieldException;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.member.dto.MembersDto;
import com.libraryman_api.member.dto.UpdateMembersDto;
import com.libraryman_api.member.dto.UpdatePasswordDto;
import com.libraryman_api.notification.NotificationService;
import com.libraryman_api.security.config.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link MemberService}.
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private NotificationService notificationService;
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    @BeforeEach
    void setup() {
        memberService = new MemberService(memberRepository, notificationService, passwordEncoder);
    }

    @Nested
    class GetAllMembers {
        @Test
        void success() {
            Pageable pageable = TestUtil.getPageRequest("name");
            Page<Members> membersPage = new PageImpl<>(List.of(TestUtil.getMembers()));
            when(memberRepository.findAll(pageable)).thenReturn(membersPage);

            Page<MembersDto> membersDtoPage = memberService.getAllMembers(pageable);

            assertEquals(1, membersDtoPage.getTotalElements());
            assertEquals(TestUtil.Constant.MEMBER_ID, membersDtoPage.getContent().get(0).getMemberId());
        }

        @Test
        void sortByFieldInvalid_throwsException() {
            Pageable pageable = TestUtil.getPageRequest("nonexistentField");
            when(memberRepository.findAll(pageable)).thenThrow(PropertyReferenceException.class);

            Exception exception = assertThrows(InvalidSortFieldException.class, () -> memberService.getAllMembers(pageable));

            assertEquals("The specified 'sortBy' value is invalid.", exception.getMessage());
        }
    }

    @Nested
    class GetMemberById {
        @Test
        void success() {
            when(memberRepository.findById(any())).thenReturn(Optional.of(TestUtil.getMembers()));

            Optional<MembersDto> membersDto = memberService.getMemberById(TestUtil.Constant.MEMBER_ID);

            assertTrue(membersDto.isPresent());
            assertEquals(TestUtil.Constant.MEMBER_ID, membersDto.get().getMemberId());
        }

        @Test
        void noMemberFound_returnsEmpty() {
            int idNotInRepository = TestUtil.getRandomInt();
            when(memberRepository.findById(any())).thenReturn(Optional.empty());

            Optional<MembersDto> membersDto = memberService.getMemberById(idNotInRepository);

            assertTrue(membersDto.isEmpty());
        }
    }

    @Test
    void addMember() {
        MembersDto membersDto = TestUtil.getMembersDto();
        when(memberRepository.save(any())).thenReturn(TestUtil.getMembers());

        MembersDto membersDtoResult = memberService.addMember(membersDto);

        assertEquals(TestUtil.Constant.MEMBER_ID, membersDtoResult.getMemberId());
    }

    @Nested
    class UpdateMember {
        @Test
        void success() {
            UpdateMembersDto membersDtoDetails = TestUtil.getUpdateMembersDto();
            Members existingMembers = TestUtil.getMembers();
            Members savedMembers = TestUtil.getMembers();
            savedMembers.setName(membersDtoDetails.getName());
            when(memberRepository.findById(any())).thenReturn(Optional.of(existingMembers));
            when(memberRepository.save(any())).thenReturn(savedMembers);

            MembersDto membersDtoResult = memberService.updateMember(TestUtil.Constant.MEMBER_ID, membersDtoDetails);

            assertEquals(membersDtoDetails.getName(), membersDtoResult.getName());
        }

        @Test
        void noMemberFound_throwsException() {
            int idNotInRepository = TestUtil.getRandomInt();
            UpdateMembersDto membersDtoDetails = TestUtil.getUpdateMembersDto();
            when(memberRepository.findById(any())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> memberService.updateMember(idNotInRepository, membersDtoDetails));

            assertEquals("Member not found", exception.getMessage());
        }
    }

    @Nested
    class DeleteMember {
        @Test
        void success() {
            when(memberRepository.findById(any())).thenReturn(Optional.of(TestUtil.getMembers()));

            assertDoesNotThrow(() -> memberService.deleteMember(TestUtil.Constant.MEMBER_ID));
        }

        @Test
        void noMemberFound_throwsException() {
            int idNotInRepository = TestUtil.getRandomInt();
            when(memberRepository.findById(any())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> memberService.deleteMember(idNotInRepository));

            assertEquals("Member not found", exception.getMessage());
        }
    }

    @Nested
    class UpdatePassword {
        @Test
        void success() {
            String oldPassword = "oldPassword";
            String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(oldPassword);
            Members member = TestUtil.getMembers();
            member.setPassword(encodedPassword);
            when(memberRepository.findById(any())).thenReturn(Optional.of(member));

            UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
            updatePasswordDto.setCurrentPassword(oldPassword);
            updatePasswordDto.setNewPassword("newPassword");

            memberService.updatePassword(TestUtil.Constant.MEMBER_ID, updatePasswordDto);

            assertTrue(passwordEncoder.bCryptPasswordEncoder().matches("newPassword", member.getPassword()));
        }

        @Test
        void memberNotFound_throwsException() {
            when(memberRepository.findById(any())).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> memberService.updatePassword(TestUtil.Constant.MEMBER_ID, TestUtil.getUpdatePasswordDto()));

            assertEquals("Member not found", exception.getMessage());
        }

        @Test
        void incorrectCurrentPassword_throwsException() {
            when(memberRepository.findById(any())).thenReturn(Optional.of(TestUtil.getMembers()));

            InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> memberService.updatePassword(TestUtil.Constant.MEMBER_ID, TestUtil.getUpdatePasswordDto()));

            assertEquals("Current password is incorrect", exception.getMessage());
        }

        @Test
        void updatePassword_newPasswordSameAsOld_throwsException() {
            String oldPassword = "oldPassword";
            String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(oldPassword);
            Members member = TestUtil.getMembers();
            member.setPassword(encodedPassword);
            when(memberRepository.findById(any())).thenReturn(Optional.of(member));

            UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
            updatePasswordDto.setCurrentPassword(oldPassword);
            updatePasswordDto.setNewPassword(oldPassword);

            InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> memberService.updatePassword(TestUtil.Constant.MEMBER_ID, updatePasswordDto));

            assertEquals("New password must be different from the old password", exception.getMessage());
        }
    }

    @Test
    void dtoEntity() {
        MembersDto membersDto = TestUtil.getMembersDto();

        Members members = memberService.DtoEntity(membersDto);

        assertEquals(membersDto.getMemberId(), members.getMemberId());
        assertEquals(membersDto.getRole(), members.getRole());
        assertEquals(membersDto.getName(), members.getName());
        assertEquals(membersDto.getUsername(), members.getUsername());
        assertEquals(membersDto.getEmail(), members.getEmail());
        assertEquals(membersDto.getPassword(), members.getPassword());
        assertEquals(membersDto.getMembershipDate(), members.getMembershipDate());
    }

    @Test
    void entityToDto() {
        Members members = TestUtil.getMembers();

        MembersDto membersDto = memberService.EntityToDto(members);

        assertEquals(members.getMemberId(), membersDto.getMemberId());
        assertEquals(members.getName(), membersDto.getName());
        assertEquals(members.getUsername(), membersDto.getUsername());
        assertEquals(members.getRole(), membersDto.getRole());
        assertEquals(members.getEmail(), membersDto.getEmail());
        assertEquals(members.getPassword(), membersDto.getPassword());
        assertEquals(members.getMembershipDate(), membersDto.getMembershipDate());
    }
}
