package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.controller.LoginController;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UtilisateurRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        String email = "test@test.com";
        String password = "password";

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(password);
        utilisateur.setId(1);

        when(userRepository.findOptionalByEmail(email)).thenReturn(Optional.of(utilisateur));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtService.generateToken(any())).thenReturn("token");

        LoginController.LoginResponse response = authService.login(email, password);

        verify(userRepository, times(1)).findOptionalByEmail(email);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(any());

        assert response.getToken().equals("token");
        assert response.getId().equals(utilisateur.getId());
        assert response.getRoles() == null;
    }

    @Test
    void shouldReturnLoginResponseWhenLoginIsCalledWithUserWithoutTeam() {
        String email = "test@test.com";
        String password = "password";
        String token = "token";

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(password);
        utilisateur.setId(1);

        when(userRepository.findOptionalByEmail(email)).thenReturn(Optional.of(utilisateur));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtService.generateToken(utilisateur)).thenReturn(token);

        LoginController.LoginResponse response = authService.login(email, password);

        verify(userRepository, times(1)).findOptionalByEmail(email);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(utilisateur);

        assertEquals(token, response.getToken());
        assertEquals(utilisateur.getId(), response.getId());
    }

    @Test
    void shouldReturnLoginResponseWhenLoginIsCalledWithUserWithTeam() {
        String email = "test@test.com";
        String password = "password";
        String token = "token";

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(password);
        utilisateur.setId(1);

        Equipe equipe = new Equipe();
        equipe.setId(2);
        utilisateur.setEquipe(equipe);

        when(userRepository.findOptionalByEmail(email)).thenReturn(Optional.of(utilisateur));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtService.generateToken(utilisateur)).thenReturn(token);

        LoginController.LoginResponse response = authService.login(email, password);

        verify(userRepository, times(1)).findOptionalByEmail(email);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(utilisateur);

        assertEquals(token, response.getToken());
        assertEquals(utilisateur.getId(), response.getId());
        assertEquals(equipe.getId(), response.getTeamId());
    }
}
