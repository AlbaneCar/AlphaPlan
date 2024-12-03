package fr.eseo.backendalphaplan.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

class LogoutServiceTest {

    @InjectMocks
    private LogoutService logoutService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogout() {
        when(request.getHeader("Authorization")).thenReturn("Bearer testToken");

        logoutService.logout(request, response, authentication);

        verify(response, times(1)).setHeader("Authorization", "");
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testLogoutWithoutHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(response, times(0)).setHeader("Authorization", "");
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}