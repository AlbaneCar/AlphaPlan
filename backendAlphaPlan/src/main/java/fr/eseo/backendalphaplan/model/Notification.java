package fr.eseo.backendalphaplan.model;

import fr.eseo.backendalphaplan.model.enums.TypeNotification;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;


/**
 * @file Notification.java
 * @brief Classe permettant de définir une notification
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 * @see Utilisateur
 * @see TypeNotification
 */
@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TypeNotification type;

    private String message;

    @ManyToOne
    @JoinColumn(name = "id_envoyeur")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur envoyeur;

    @ManyToOne
    @JoinColumn(name = "id_receveur")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur receveur;

    @Temporal(TemporalType.DATE)
    private Date dateSignalement;

    /**
     * @brief Méthode permettant de définir la date de signalement
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    @PrePersist
    protected void onCreate() {
        dateSignalement = new Date();
    }
}
