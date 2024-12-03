package fr.eseo.backendalphaplan.model;

import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @file NoteEleve.java
 * @brief Classe permettant de définir une note d'un élève
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 * @see Utilisateur
 * @see Sprint
 */
@Entity
@Data
// @EntityListeners(NoteEleveListener.class)
public class NoteEleve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TypeNoteEleve typeNoteEleve;
    
    @JoinColumn(name = "note")
    private Double note;
    
    @JoinColumn(name = "commentaire")
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur eleve;

    @ManyToOne
    @JoinColumn(name = "evaluateur_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur evaluateur;

    @ManyToOne
    @JoinColumn(name ="sprint_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sprint sprint;

    public static TypeNoteEleve convertStringToEnum(String typeNoteEleveString) {
        return switch (typeNoteEleveString) {
            case "SS_PR" -> TypeNoteEleve.SS_PR;
            case "IN_PR" -> TypeNoteEleve.IN_PR;
            case "TE_BM" -> TypeNoteEleve.TE_BM;
            case "IN_SP" -> TypeNoteEleve.IN_SP;
            case "SS_BM" -> TypeNoteEleve.SS_BM;
            case "IG_SP" -> TypeNoteEleve.IG_SP;
            default -> null;
        };
    }

}
