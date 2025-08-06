package com.libraryman_api.security.jwt;

import com.libraryman_api.TestUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link JwtAuthenticationHelper}.
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationHelperTest {
    private JwtAuthenticationHelper jwtAuthenticationHelper;
    private String secret;

    @BeforeEach
    void setup() {
        secret = "aVeryLongSecretStringThatIsAtLeast64BytesLongAndSecureEnoughForHS512";
        jwtAuthenticationHelper = new JwtAuthenticationHelper(secret);
    }

    @Nested
    class GetUsernameFromToken {
        @Test
        void success() {
            String expectedUsername = "User";
            String token = Jwts.builder().signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256).setSubject(expectedUsername).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 100000)).compact();

            String actualUsername = jwtAuthenticationHelper.getUsernameFromToken(token);

            assertEquals(expectedUsername, actualUsername);
        }

        @Test
        void expired() {
            Date date = new Date();
            String token = Jwts.builder().signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256).setSubject("User").setIssuedAt(date).setExpiration(date).compact();

            assertThrows(ExpiredJwtException.class, () -> jwtAuthenticationHelper.getUsernameFromToken(token));
        }

        @Test
        void malformed() {
            assertThrows(MalformedJwtException.class, () -> jwtAuthenticationHelper.getUsernameFromToken("malformed.token"));
        }

        @Test
        void signatureException() {
            String differentSecret = "notTheSameSecretAsTheSecretValueInTheHelper";
            String token = Jwts.builder().signWith(Keys.hmacShaKeyFor(differentSecret.getBytes()), SignatureAlgorithm.HS256).setSubject("User").setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 100000)).compact();

            assertThrows(SignatureException.class, () -> jwtAuthenticationHelper.getUsernameFromToken(token));
        }
    }

    @Test
    void generateToken() {
        UserDetails userDetails = TestUtil.getMembers();

        String token = jwtAuthenticationHelper.generateToken(userDetails);

        Claims claims = jwtAuthenticationHelper.getClaimsFromToken(token);
        assertEquals(userDetails.getUsername(), claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }
}
