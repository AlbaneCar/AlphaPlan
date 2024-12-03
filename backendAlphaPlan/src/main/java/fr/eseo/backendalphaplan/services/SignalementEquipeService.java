package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Notification;
import fr.eseo.backendalphaplan.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @file SignalementEquipeService.java
 * @brief Service pour les signalements d'équipe
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
public class SignalementEquipeService {

    // Attributs
    private final NotificationRepository notificationRepository;

    // Constructeur
    @Autowired
    public SignalementEquipeService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * @brief Sauvegarde un signalement d'équipe
     * @param signalementEquipe : signalement d'équipe
     * @return Notification
     */
    public Notification saveSignalementEquipe(Notification signalementEquipe) {
        return notificationRepository.save(signalementEquipe);
    }
}
