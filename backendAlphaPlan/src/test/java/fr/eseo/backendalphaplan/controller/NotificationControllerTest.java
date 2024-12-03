package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.Notification;
import fr.eseo.backendalphaplan.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNotificationById() {
        Notification expected = new Notification();
        when(notificationService.getNotificationById(1)).thenReturn(expected);

        Notification result = notificationController.getNotificationById(1);

        assertEquals(expected, result);
        verify(notificationService, times(1)).getNotificationById(1);
    }

    @Test
    void testGetNotifications() {
        List<Notification> expected = new ArrayList<>();
        when(notificationService.getNotifications()).thenReturn(expected);

        List<Notification> result = notificationController.getNotifications();

        assertEquals(expected, result);
        verify(notificationService, times(1)).getNotifications();
    }

    @Test
    void testAddStudent() {
        Notification expected = new Notification();
        when(notificationService.saveNotification(expected)).thenReturn(expected);

        ResponseEntity<Notification> result = notificationController.addStudent(expected);

        assertEquals(expected, result.getBody());
        verify(notificationService, times(1)).saveNotification(expected);
    }
    @Test
    void getNotificationsByUtilisateurReturnsOkWhenUserHasNotifications() {
        List<Notification> expected = new ArrayList<>();
        when(notificationService.getNotificationsByUtilisateur(1)).thenReturn(expected);

        ResponseEntity<List<Notification>> result = notificationController.getNotificationsByUtilisateur(1);

        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void deleteNotificationReturnsNotFoundWhenNotificationDoesNotExist() {
        when(notificationService.getNotificationById(1)).thenReturn(null);

        ResponseEntity<Notification> result = notificationController.deleteNotification(1);

        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    void creerNotifReturnsBadRequestWhenInvalidData() {
        NotificationController.NotificationRequest request = new NotificationController.NotificationRequest();
        when(notificationService.createNotification(request)).thenThrow(IllegalArgumentException.class);

        ResponseEntity<NotificationController.NotificationResponse> result = notificationController.creerNotif(request);

        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    void testDeleteNotification_NotificationExists() {
        // Prepare test data
        Integer id = 1;
        Notification notification = new Notification();
        notification.setId(id);

        // Mock the behavior of notificationService
        when(notificationService.getNotificationById(id)).thenReturn(notification);

        // Call the method under test
        ResponseEntity<Notification> result = notificationController.deleteNotification(id);

        // Verify the results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(notification, result.getBody());

        // Verify the interactions with the mock
        verify(notificationService, times(1)).getNotificationById(id);
        verify(notificationService, times(1)).deleteNotification(notification);
    }

    @Test
    void testDeleteNotification_NotificationDoesNotExist() {
        // Prepare test data
        Integer id = 1;

        // Mock the behavior of notificationService
        when(notificationService.getNotificationById(id)).thenReturn(null);

        // Call the method under test
        ResponseEntity<Notification> result = notificationController.deleteNotification(id);

        // Verify the results
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        // Verify the interactions with the mock
        verify(notificationService, times(1)).getNotificationById(id);
        verify(notificationService, never()).deleteNotification(any());
    }

    @Test
    void testCreerNotif() {
        // Prepare test data
        NotificationController.NotificationRequest request = new NotificationController.NotificationRequest();
        Notification expectedNotification = new Notification();
        expectedNotification.setId(1);

        // Mock the behavior of notificationService
        when(notificationService.createNotification(request)).thenReturn(expectedNotification);

        // Call the method under test
        ResponseEntity<NotificationController.NotificationResponse> result = notificationController.creerNotif(request);

        // Verify the results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedNotification, result.getBody().getNotification());
        assertEquals("Notification créée", result.getBody().getMessage());

        // Verify the interactions with the mock
        verify(notificationService, times(1)).createNotification(request);
    }


}

class NotificationResponseTest {

    @Test
    void testNotificationResponse() {
        // Prepare test data
        Notification notification = new Notification();
        notification.setId(1);
        String message = "Test message";

        // Create an instance of NotificationResponse
        NotificationController.NotificationResponse response = NotificationController.NotificationResponse.builder()
                .notification(notification)
                .message(message)
                .build();

        // Verify the results
        assertEquals(notification, response.getNotification());
        assertEquals(message, response.getMessage());
    }

    @Test
    void testNotificationResponseData() {
        // Prepare test data
        Notification notification = new Notification();
        notification.setId(1);
        String message = "Test message";

        // Create an instance of NotificationResponse using the no-args constructor
        NotificationController.NotificationResponse response = new NotificationController.NotificationResponse();

        // Set the fields
        response.setNotification(notification);
        response.setMessage(message);

        // Verify the results
        assertEquals(notification, response.getNotification());
        assertEquals(message, response.getMessage());
    }

    @Test
    void testRequiredArgsConstructor() {
        // Create an instance of NotificationResponse using the no-args constructor
        NotificationController.NotificationResponse response = new NotificationController.NotificationResponse();

        // Verify the results
        assertNotNull(response);
    }

}

class NotificationRequestTest {

    @Test
    void testNotificationRequest() {


        // Prepare test data
        String type = "Test Type";
        String message = "Test Message";
        Integer envoyeur = 1;
        Integer receveur = 2;

        // Create an instance of NotificationRequest
        NotificationController.NotificationRequest request = NotificationController.NotificationRequest.builder()
                .type(type)
                .message(message)
                .envoyeur(envoyeur)
                .receveur(receveur)
                .build();

        // Verify the results
        assertEquals(type, request.getType());
        assertEquals(message, request.getMessage());
        assertEquals(envoyeur, request.getEnvoyeur());
        assertEquals(receveur, request.getReceveur());
    }

    @Test
    void testNotificationRequestData() {
        // Create an instance of NotificationRequest
        NotificationController.NotificationRequest request = new NotificationController.NotificationRequest();

        // Set the fields
        String type = "Test Type";
        String message = "Test Message";
        Integer envoyeur = 1;
        Integer receveur = 2;

        request.setType(type);
        request.setMessage(message);
        request.setEnvoyeur(envoyeur);
        request.setReceveur(receveur);

        // Verify the results
        assertEquals(type, request.getType());
        assertEquals(message, request.getMessage());
        assertEquals(envoyeur, request.getEnvoyeur());
        assertEquals(receveur, request.getReceveur());
    }
}
