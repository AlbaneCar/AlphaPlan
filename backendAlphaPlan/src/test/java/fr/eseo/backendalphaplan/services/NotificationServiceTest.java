package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.controller.NotificationController.NotificationRequest;
import fr.eseo.backendalphaplan.model.Notification;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNotification;
import fr.eseo.backendalphaplan.repository.NotificationRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;
    
    @Mock
    private UtilisateurRepository utilisateurRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNotificationById() {
        Notification notification = new Notification();
        when(notificationRepository.findById(anyInt())).thenReturn(Optional.of(notification));

        var result = notificationService.getNotificationById(1);

        assertEquals(notification, result);
    }

    @Test
    void testGetNotifications() {
        Notification notification = new Notification();
        when(notificationRepository.findAll()).thenReturn(Collections.singletonList(notification));

        var result = notificationService.getNotifications();

        assertEquals(1, result.size());
    }

    @Test
    void testSaveNotification() {
        Notification notification = new Notification();
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        var result = notificationService.saveNotification(notification);

        assertEquals(notification, result);
    }
    
    @Test
    void testGetNotificationsByUtilisateur() {
        Notification notification = new Notification();
        when(notificationRepository.findAllByReceveur_Id(anyInt())).thenReturn(Collections.singletonList(notification));

        List<Notification> result = notificationService.getNotificationsByUtilisateur(1);

        assertEquals(1, result.size());
        assertEquals(notification, result.get(0));
    }

    @Test
    void testDeleteNotification() {
        Notification notification = new Notification();

        notificationService.deleteNotification(notification);

        verify(notificationRepository, times(1)).delete(notification);
    }

    @Test
    void testExceptionInvalidReceveur() {
        NotificationRequest request = new NotificationRequest();
        request.setReceveur(1);
        request.setEnvoyeur(2);
        request.setMessage("Test message");
        request.setType("INFO");

        when(utilisateurRepository.findById(1)).thenReturn(Optional.empty());
        when(utilisateurRepository.findById(2)).thenReturn(Optional.of(new Utilisateur()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationService.createNotification(request);
        });

        assertEquals("Le receveur n'existe pas", exception.getMessage());
    }

    @Test
    void testExceptionInvalidEnvoyeur() {
        NotificationRequest request = new NotificationRequest();
        request.setReceveur(1);
        request.setEnvoyeur(2);
        request.setMessage("Test message");
        request.setType("INFO");

        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(new Utilisateur()));
        when(utilisateurRepository.findById(2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationService.createNotification(request);
        });

        assertEquals("L'envoyeur n'existe pas", exception.getMessage());
    }
}