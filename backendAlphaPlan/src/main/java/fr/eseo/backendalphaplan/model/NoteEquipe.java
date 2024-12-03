package fr.eseo.backendalphaplan.model;

import fr.eseo.backendalphaplan.model.enums.TypeNoteEquipe;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;
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
public class NoteEquipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "type_note_equipe")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TypeNoteEquipe typeNoteEquipe;

    @JoinColumn(name = "note")
    private Double note;

    @JoinColumn(name = "commentaire")
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "equipe_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Equipe equipe;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name = "evaluateur_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur evaluateur;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "type_echelle")
    private TypeEchelle typeEchelle;

    /**
     * @brief Méthode permettant de convertir une chaîne de caractères en énumération
     * @param typeNoteEquipeString : String : Chaîne de caractères à convertir
     * @return TypeNoteEquipe : Énumération convertie
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public static TypeNoteEquipe convertStringToEnum(String typeNoteEquipeString) {
        if (typeNoteEquipeString.equals("SU_PR")) {
            return TypeNoteEquipe.SU_PR;
        } else if (typeNoteEquipeString.equals("SP_CO")) {
            return TypeNoteEquipe.SP_CO;
        } else if (typeNoteEquipeString.equals("TE_SO")) {
            return TypeNoteEquipe.TE_SO;
        } else if (typeNoteEquipeString.equals("PR_MA")) {
            return TypeNoteEquipe.PR_MA;
        } else if (typeNoteEquipeString.equals("TE_WO")) {
            return TypeNoteEquipe.TE_WO;
        } else if (typeNoteEquipeString.equals("OT_PR")) {
            return TypeNoteEquipe.OT_PR;
        } else {
            throw new IllegalArgumentException("Valeur d'énumération non valide : " + typeNoteEquipeString);
        }
    }
}
