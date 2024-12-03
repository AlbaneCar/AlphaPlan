package fr.eseo.backendalphaplan.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @file EchelleNote.java
 * @brief Définition de la classe EchelleNote.
 * @version 1.0
 */
@Entity
@Getter
@Setter
@Data
public class EchelleNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "type_echelle")
    private TypeEchelle typeEchelle;

    @JoinColumn (name = "description")
    private String description;

    @OneToMany(mappedBy = "echelleNoteId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Echelon> echelons;

    /**
     * @brief Méthode toString
     * @return String : la chaine de caractère
     */
    @Override
    public String toString() {
    return "EchelleNote{" +
            "id=" + id +
            ", typeEchelle=" + typeEchelle +
            ", description='" + description + '\'' +
            '}';
    }
}
