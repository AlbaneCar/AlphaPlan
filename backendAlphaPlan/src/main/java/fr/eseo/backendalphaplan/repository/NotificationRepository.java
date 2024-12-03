package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @file NotificationRepository.java
 * @brief Interface repository pour les Notification
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    /**
     * @brief Requête pour récupérer les notifications d'un receveur
     * @param id : id du receveur
     * @return Liste des Notifications
     */
    List<Notification> findAllByReceveur_Id(Integer id);
}
