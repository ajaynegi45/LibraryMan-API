package com.libraryman_api.security.jwt;

import com.libraryman_api.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link JwtAuthenticationFilter}.
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @Mock
    private JwtAuthenticationHelper jwtAuthenticationHelper;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class DoFilterInternal {
        @Test
        void success() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer valid.token.here");

            when(jwtAuthenticationHelper.getUsernameFromToken(any())).thenReturn("user");
            when(userDetailsService.loadUserByUsername(any())).thenReturn(TestUtil.getMembers());
            when(jwtAuthenticationHelper.isTokenExpired(any())).thenReturn(false);

            assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain()));

            assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        void noAuthorizationHeader() {
            MockHttpServletRequest request = new MockHttpServletRequest();

            assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain()));

            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        void notBearer() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Basic encoded.credentials");

            assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain()));

            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @ParameterizedTest
        @CsvSource({"io.jsonwebtoken.ExpiredJwtException", "io.jsonwebtoken.MalformedJwtException", "io.jsonwebtoken.SignatureException"})
        void getUsernameFromToken_throwsException(String exceptionClassName) throws ClassNotFoundException {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer some.token.here");
            Class<? extends Exception> exception = (Class<? extends Exception>) Class.forName(exceptionClassName);
            when(jwtAuthenticationHelper.getUsernameFromToken(any())).thenThrow(exception);

            assertThrows(exception, () -> jwtAuthenticationFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain()));

            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        void nullUsername() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer valid.token.here");
            when(jwtAuthenticationHelper.getUsernameFromToken(any())).thenReturn(null);

            assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain()));

            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        void usernameNotFoundException() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer valid.token.here");

            when(jwtAuthenticationHelper.getUsernameFromToken(any())).thenReturn("user");
            when(userDetailsService.loadUserByUsername(any())).thenThrow(UsernameNotFoundException.class);

            assertThrows(UsernameNotFoundException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain()));

            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        void tokenExpired() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer valid.token.here");

            when(jwtAuthenticationHelper.getUsernameFromToken(any())).thenReturn("user");
            when(userDetailsService.loadUserByUsername(any())).thenReturn(TestUtil.getMembers());
            when(jwtAuthenticationHelper.isTokenExpired(any())).thenReturn(true);

            assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain()));

            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        void securityContextAlreadyExists() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer valid.token.here");

            when(jwtAuthenticationHelper.getUsernameFromToken(any())).thenReturn("user");

            Authentication existingAuth = mock(Authentication.class);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(existingAuth);
            SecurityContextHolder.setContext(context);
            assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain()));

            assertEquals(existingAuth, SecurityContextHolder.getContext().getAuthentication());
        }
    }
}
