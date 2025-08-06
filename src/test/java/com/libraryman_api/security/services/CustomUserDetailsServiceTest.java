package com.libraryman_api.security.services;

import com.libraryman_api.TestUtil;
import com.libraryman_api.member.MemberRepository;
import com.libraryman_api.member.Members;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link CustomUserDetailsService}.
 */
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Nested
    class LoadUserByUsername {
        @Test
        void success() {
            Members members = TestUtil.getMembers();
            when(memberRepository.findByUsername(any())).thenReturn(Optional.of(members));

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(members.getUsername());

            assertEquals(members, userDetails);
        }

        @Test
        void notFound() {
            when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("username"));
        }
    }
}
