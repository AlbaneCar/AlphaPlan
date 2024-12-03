package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.Notification;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNotification;
import fr.eseo.backendalphaplan.services.SignalementEquipeService;
import fr.eseo.backendalphaplan.services.UtilisateurService;
import fr.eseo.backendalphaplan.utils.SignalementEquipeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;



import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SignalementEquipeControllerTest {

    @InjectMocks
    private SignalementEquipeController signalementEquipeController;

    @Mock
    private SignalementEquipeService signalementEquipeService;

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Test
    void saveSignalementEquipeReturnsUnauthorizedWhenUsernamesDoNotMatch() throws IOException {
        SignalementEquipeRequest request = new SignalementEquipeRequest();
        request.setUtilisateurId(1);
        request.setDescription("Test description");

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom ("differentUser");

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(utilisateurService.getUtilisateurById(1)).thenReturn(utilisateur);

        ResponseEntity<Notification> result = signalementEquipeController.saveSignalementEquipe(null, request);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getStatusCodeValue());
    }
    @Test
    void saveSignalementEquipeReturnsOkWhenUsernamesMatch() throws IOException {
        // Prepare test data
        SignalementEquipeRequest request = new SignalementEquipeRequest();
        request.setUtilisateurId(1);
        request.setDescription("Test description");

        Utilisateur utilisateur = mock(Utilisateur.class);
        Notification notification = mock(Notification.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(utilisateurService.getUtilisateurById(1)).thenReturn(utilisateur);
        when(utilisateur.getUsername()).thenReturn("testUser");
        when(utilisateurService.getUtilisateurByEmail("admin@mail.com")).thenReturn(utilisateur);
        when(signalementEquipeService.saveSignalementEquipe(any(Notification.class))).thenReturn(notification);

        // Call the method under test
        ResponseEntity<Notification> result = signalementEquipeController.saveSignalementEquipe(null, request);

        // Verify the results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(notification, result.getBody());

        // Verify the interactions with the mock
        verify(utilisateurService, times(2)).getUtilisateurById(1);
        verify(utilisateurService, times(1)).getUtilisateurByEmail("admin@mail.com");
        verify(signalementEquipeService, times(1)).saveSignalementEquipe(any(Notification.class));
    }

}