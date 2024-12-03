package fr.eseo.backendalphaplan.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

/**
 * @file OrdrePassage.java
 * @brief Définition de la classe OrdrePassage.
 * @version 1.0
 * @author Hugo BOURDAIS & Antoine MORIN
 * @date 01/04/2024
 */
@Entity
@Data
public class OrdrePassage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "equipe")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Equipe equipe;

    @ManyToOne
    @JoinColumn(name = "sprint")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name = "auteur")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur auteur;

    @JoinColumn(name = "date_creation")
    private LocalDate dateCreation = LocalDate.now();

    @ManyToMany
    @JoinTable(
        name = "ordre_passage_utilisateur",
        joinColumns = @JoinColumn(name = "ordre_passage_id"),
        inverseJoinColumns = @JoinColumn(name = "utilisateur_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Utilisateur> ordre;
}