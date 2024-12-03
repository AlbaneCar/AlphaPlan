package fr.eseo.backendalphaplan.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);  // Set jwtExpiration to 1 hour
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "A8VNZGZ1ZuyRzE+HfpKxKVftAieaPLYTm0Oy+smzudoRfn248/8U5RpTw6N+SkMNSMx/xElIChgsFEuw3VIXBQ=="); // Set jwtSecret
    }

    @Test
    void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertEquals("testUser", jwtService.extractUsername(token));
    }

    @Test
    void testValidateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.validateToken(token, userDetails));
    }

    @Test
    void testSetExpiredToken() {
        // Prepare test data
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);

        // Call the method under test
        String expiredToken = jwtService.setExpiredToken(token);

        // Verify the results
        // The token should be expired, so the validation should fail
        try {
            jwtService.validateToken(expiredToken, userDetails);
            fail("Expected an ExpiredJwtException to be thrown");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // A ExpiredJwtException was thrown, as expected
        }
    }
}