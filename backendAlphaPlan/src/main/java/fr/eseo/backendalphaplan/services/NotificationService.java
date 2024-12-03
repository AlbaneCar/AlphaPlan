package fr.eseo.backendalphaplan.services;


import fr.eseo.backendalphaplan.model.Notification;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNotification;
import fr.eseo.backendalphaplan.repository.NotificationRepository;
import fr.eseo.backendalphaplan.controller.NotificationController.NotificationRequest;

import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @file NotificationService.java
 * @brief Service pour les Notifications
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
public class NotificationService {

    // Attributs
    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository;

    // Constructeur
    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UtilisateurRepository utilisateurRepository) {
        this.notificationRepository = notificationRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * @brief Récupérer une notification via son id
     * @param id : id de la notification
     * @return Notification
     */
    public Notification getNotificationById(Integer id) {
        return notificationRepository.findById(id).orElse(null);
    }

    /**
     * @brief Récupérer toutes les notifications
     * @return Liste des Notifications
     */
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    /**
     * @brief Récupérer les notifications d'un utilisateur
     * @param id : id de l'utilisateur
     * @return Liste des Notifications
     */
    public List<Notification> getNotificationsByUtilisateur(Integer id) {
        return notificationRepository.findAllByReceveur_Id(id);
    }

    /**
     * @brief Sauvegarder une notification
     * @param notification : notification à sauvegarder
     * @return Notification
     */
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * @param notification : notification à supprimer
     * @brief Supprimer une notification
     */
    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    /**
     * @param request : requête de la notification
     * @return NotificationResponse
     * @brief Créer une notification
     */
    public Notification createNotification(NotificationRequest request) throws IllegalArgumentException {
        // On vérifie que le receveur existe
        Optional<Utilisateur> receveur = utilisateurRepository.findById(request.getReceveur());
        if (receveur.isEmpty()) {
            throw new IllegalArgumentException("Le receveur n'existe pas");
        }
        // On vérifie que l'envoyeur existe
        Optional<Utilisateur> envoyeur = utilisateurRepository.findById(request.getEnvoyeur());
        if (envoyeur.isEmpty()) {
            throw new IllegalArgumentException("L'envoyeur n'existe pas");
        }
        // On crée la notification
        Notification notification = new Notification();
        notification.setMessage(request.getMessage());
        notification.setEnvoyeur(envoyeur.get());
        notification.setReceveur(receveur.get());
        notification.setType(TypeNotification.getType(request.getType()));
        // On sauvegarde la notification
        return notificationRepository.save(notification);
    }
}
