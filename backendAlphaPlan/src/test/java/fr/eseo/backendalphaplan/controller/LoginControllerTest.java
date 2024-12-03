package fr.eseo.backendalphaplan.controller;


import fr.eseo.backendalphaplan.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;


class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void loginReturnsBadRequestWhenEmailIsMissing() {
        ResponseEntity<LoginController.LoginResponse> result = loginController.login(new LoginController.LoginRequest(null, "password"));

        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void loginReturnsBadRequestWhenPasswordIsMissing() {
        ResponseEntity<LoginController.LoginResponse> result = loginController.login(new LoginController.LoginRequest("email", null));

        assertEquals(200, result.getStatusCodeValue());
    }
}