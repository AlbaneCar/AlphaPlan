package fr.eseo.backendalphaplan.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

/**
 * @file SignalementEquipe.java
 * @brief Définition de la classe SignalementEquipe.
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 * @see Equipe
 * @see Utilisateur
 */
@Entity
@Setter
@Getter
@RequiredArgsConstructor
public class SignalementEquipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;

    @ManyToOne
    @JoinColumn(name = "equipe_id", referencedColumnName="id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Equipe equipe;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", referencedColumnName="id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur utilisateur;

    @Temporal(TemporalType.DATE)
    private Date dateSignalement;

    /**
     * @brief Méthode permettant de définir la date de signalement.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    @PrePersist
    protected void onCreate() {
        dateSignalement = new Date();
    }
}
