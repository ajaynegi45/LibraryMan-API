package com.libraryman_api.security.services;

import com.libraryman_api.TestUtil;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.member.MemberRepository;
import com.libraryman_api.member.Members;
import com.libraryman_api.member.Role;
import com.libraryman_api.security.config.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link SignupService}.
 */
@ExtendWith(MockitoExtension.class)
class SignupServiceTest {
    @Mock
    private MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();
    private SignupService signupService;

    @Captor
    private ArgumentCaptor<Members> membersCaptor;

    @BeforeEach
    void setup() {
        signupService = new SignupService(memberRepository, passwordEncoder);
    }

    @Nested
    class Signup {
        @Test
        void success() {
            Members members = TestUtil.getMembers();
            when(memberRepository.findById(any())).thenReturn(Optional.empty());
            when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

            signupService.signup(members);

            verify(memberRepository).save(membersCaptor.capture());
            Members savedMembers = membersCaptor.getValue();
            assertEquals(members.getUsername(), savedMembers.getUsername());
            assertEquals(members.getName(), savedMembers.getName());
            assertEquals(members.getEmail(), savedMembers.getEmail());
            assertEquals(Role.USER, savedMembers.getRole());
            assertNotNull(savedMembers.getMembershipDate());
            assertNotNull(savedMembers.getPassword());
        }

        @Test
        void idFound() {
            Members members = TestUtil.getMembers();
            when(memberRepository.findById(any())).thenReturn(Optional.of(members));
            when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> signupService.signup(members));

            assertEquals("User already Exists", exception.getMessage());
        }

        @Test
        void usernameFound() {
            Members members = TestUtil.getMembers();
            when(memberRepository.findById(any())).thenReturn(Optional.empty());
            when(memberRepository.findByUsername(any())).thenReturn(Optional.of(members));

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> signupService.signup(members));

            assertEquals("User already Exists", exception.getMessage());
        }
    }

    @Nested
    class SignupAdmin {
        @Test
        void success() {
            Members members = TestUtil.getMembers();
            when(memberRepository.findById(any())).thenReturn(Optional.empty());
            when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

            signupService.signupAdmin(members);

            verify(memberRepository).save(membersCaptor.capture());
            Members savedMembers = membersCaptor.getValue();
            assertEquals(members.getUsername(), savedMembers.getUsername());
            assertEquals(members.getName(), savedMembers.getName());
            assertEquals(members.getEmail(), savedMembers.getEmail());
            assertEquals(Role.ADMIN, savedMembers.getRole());
            assertNotNull(savedMembers.getMembershipDate());
            assertNotNull(savedMembers.getPassword());
        }

        @Test
        void idFound() {
            Members members = TestUtil.getMembers();
            when(memberRepository.findById(any())).thenReturn(Optional.of(members));
            when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> signupService.signupAdmin(members));

            assertEquals("User already Exists", exception.getMessage());
        }

        @Test
        void usernameFound() {
            Members members = TestUtil.getMembers();
            when(memberRepository.findById(any())).thenReturn(Optional.empty());
            when(memberRepository.findByUsername(any())).thenReturn(Optional.of(members));

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> signupService.signupAdmin(members));

            assertEquals("User already Exists", exception.getMessage());
        }
    }

    @Nested
    class SignupLibrarian {
        @Test
        void success() {
            Members members = TestUtil.getMembers();
            when(memberRepository.findById(any())).thenReturn(Optional.empty());
            when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

            signupService.signupLibrarian(members);

            verify(memberRepository).save(membersCaptor.capture());
            Members savedMembers = membersCaptor.getValue();
            assertEquals(members.getUsername(), savedMembers.getUsername());
            assertEquals(members.getName(), savedMembers.getName());
            assertEquals(members.getEmail(), savedMembers.getEmail());
            assertEquals(Role.LIBRARIAN, savedMembers.getRole());
            assertNotNull(savedMembers.getMembershipDate());
            assertNotNull(savedMembers.getPassword());
        }

        @Test
        void idFound() {
            Members members = TestUtil.getMembers();
            when(memberRepository.findById(any())).thenReturn(Optional.of(members));
            when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> signupService.signupLibrarian(members));

            assertEquals("User already Exists", exception.getMessage());
        }

        @Test
        void usernameFound() {
            Members members = TestUtil.getMembers();
            when(memberRepository.findById(any())).thenReturn(Optional.empty());
            when(memberRepository.findByUsername(any())).thenReturn(Optional.of(members));

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> signupService.signupLibrarian(members));

            assertEquals("User already Exists", exception.getMessage());
        }
    }
}