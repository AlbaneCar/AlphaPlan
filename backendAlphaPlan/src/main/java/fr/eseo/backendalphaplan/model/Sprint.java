package fr.eseo.backendalphaplan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eseo.backendalphaplan.model.enums.FinSprint;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @file Sprint.java
 * @brief Classe permettant de définir un sprint
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 * @see FinSprint
 */
@Entity
@Data
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;


    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private FinSprint sprintEndType;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<NoteEquipe> notes = new ArrayList<>();

    /**
     * @brief Méthode permettant de convertir une chaîne de caractères en énumération.
     * @param finSprintString : String : Chaîne de caractères à convertir.
     * @return FinSprint : Énumération correspondante.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public static FinSprint convertStringToEnum(String finSprintString) {
        if (finSprintString.equals("Sprint Review")) {
            return FinSprint.SPRINT_REVIEW;
        } else if (finSprintString.equals("Présentation")) {
            return FinSprint.PRESENTATION;
        } else {
            throw new IllegalArgumentException("Valeur d'énumération non valide : " + finSprintString);
        }
    }

}
