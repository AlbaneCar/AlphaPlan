package fr.eseo.backendalphaplan.model;

import fr.eseo.backendalphaplan.model.enums.Matiere;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @file NoteAnterieure.java
 * @brief Classe permettant de définir une note antérieure
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 * @see Matiere
 * @see Utilisateur
 */
@Entity
@Data
public class NoteAnterieure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private float note;

    private float coef;

    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    private Matiere matiere;

}
