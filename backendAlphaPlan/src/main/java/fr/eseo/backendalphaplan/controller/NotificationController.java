package fr.eseo.backendalphaplan.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.eseo.backendalphaplan.model.Notification;
import fr.eseo.backendalphaplan.services.NotificationService;

import java.util.List;

/**
 * @file NotificationController.java
 * @brief Définition de la classe NotificationController.
 * @version 1.0
 */
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    // Attributs
    private final NotificationService notificationService;

    // Constructeur
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * @param id : id de la notification
     * @return Notification
     * @brief Méthode permettant de récupérer une notification par son id
     */
    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable Integer id) {
        return notificationService.getNotificationById(id);
    }

    /**
     * @return Liste des notifications
     * @brief Méthode permettant de récupérer toutes les notifications
     */
    @GetMapping
    public List<Notification> getNotifications() {
        return notificationService.getNotifications();
    }

    /**
     * @param id : id de l'utilisateur
     * @return Liste des notifications de l'utilisateur
     * @brief Méthode permettant de récupérer les notifications d'un utilisateur
     */
    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<List<Notification>> getNotificationsByUtilisateur(@PathVariable Integer id) {
        return ResponseEntity.ok(notificationService.getNotificationsByUtilisateur(id));
    }

    /**
     * @param notif : notification
     * @return Notification
     * @brief Méthode permettant d'ajouter une notification
     */
    @PostMapping("/ajouter")
    public ResponseEntity<Notification> addStudent(@RequestBody Notification notif) {
        Notification newNotif = notificationService.saveNotification(notif);
        return new ResponseEntity<>(newNotif, HttpStatus.CREATED);
    }

    /**
     * @param id : id de la notification
     * @return Notification
     * @brief Méthode permettant de supprimer une notification
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Notification> deleteNotification(@PathVariable Integer id) {
        Notification notif = notificationService.getNotificationById(id);
        if (notif != null) {
            notificationService.deleteNotification(notif);
            return new ResponseEntity<>(notif, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * @param notif : notification à créer
     * @return NotificationResponse
     * @brief Méthode permettant de créer une notification
     */
    @PostMapping("/creer")
    public ResponseEntity<NotificationResponse> creerNotif(@RequestBody NotificationRequest notif) {
        try {
            Notification notification = notificationService.createNotification(notif);
            return ResponseEntity.ok(NotificationResponse.builder().notification(notification).message("Notification créée").build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(NotificationResponse.builder().message(e.getMessage()).build());
        }
    }

    /**
     * @brief Classe permettant de définir une réponse de notification
     * @autor Hugo BOURDAIS
     * @date 03/06/2024
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Builder
    public static final class NotificationResponse {
        private Notification notification;
        private String message;
    }

    /**
     * @brief Classe permettant de définir une requête de notification
     * @autor Hugo BOURDAIS
     * @date 03/06/2024
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Builder
    public static final class NotificationRequest {
        private String type;
        private String message;
        private Integer envoyeur;
        private Integer receveur;
    }
}
