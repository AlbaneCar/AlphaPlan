package fr.eseo.backendalphaplan.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
/**
 * Classe représentant un échelon d'une échelle de notes.
 * Un échelon est défini par un commentaire et deux notes.
 */
@Entity
@Getter
@Setter
@Data
public class Echelon {

    /**
     * Identifiant unique de l'échelon.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Commentaire associé à l'échelon.
     */
    @JoinColumn (name = "commentaire")
    private String commentaire;

    /**
     * Première note de l'échelon.
     */
    @JoinColumn (name = "note1")
    private int note1;

    /**
     * Deuxième note de l'échelon.
     */
    @JoinColumn (name = "note2")
    private int note2;

    /**
     * Échelle de notes à laquelle appartient l'échelon.
     */
    @ManyToOne
    @JoinColumn (name = "EchelleNote_id", referencedColumnName="id")
    @JsonBackReference
    private EchelleNote echelleNoteId;

    /**
     * Méthode pour obtenir une représentation sous forme de chaîne de caractères de l'échelon.
     * @return une chaîne de caractères représentant l'échelon.
     */
    @Override
    public String toString() {
        return "Echelon{" +
                "id=" + id +
                ", commentaire='" + commentaire + '\'' +
                ", note1=" + note1 +
                ", note2=" + note2 +
                ", echelleNoteId=" + echelleNoteId +
                '}';
    }
}
