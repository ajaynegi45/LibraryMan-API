package com.libraryman_api.security.services;

import com.libraryman_api.TestUtil;
import com.libraryman_api.member.MemberRepository;
import com.libraryman_api.member.Members;
import com.libraryman_api.security.jwt.JwtAuthenticationHelper;
import com.libraryman_api.security.model.LoginRequest;
import com.libraryman_api.security.model.LoginResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link LoginService}.
 */
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtAuthenticationHelper jwtHelper;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private LoginService loginService;

    @Nested
    class Login {
        @Test
        void success() {
            LoginRequest loginRequest = TestUtil.getLoginRequest();
            String token = "jwtToken";
            Members members = TestUtil.getMembers();

            when(userDetailsService.loadUserByUsername(any())).thenReturn(members);
            when(jwtHelper.generateToken(members)).thenReturn(token);

            LoginResponse response = loginService.login(loginRequest);

            assertEquals(token, response.getToken());
        }

        @Test
        void badCredentials() {
            when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

            assertThrows(BadCredentialsException.class, () -> loginService.login(TestUtil.getLoginRequest()));
        }

        @Test
        void userNotFound() {
            when(userDetailsService.loadUserByUsername(any())).thenThrow(UsernameNotFoundException.class);

            assertThrows(UsernameNotFoundException.class, () -> loginService.login(TestUtil.getLoginRequest()));
        }
    }
}
