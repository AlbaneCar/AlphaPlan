package fr.eseo.backendalphaplan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import fr.eseo.backendalphaplan.model.Sprint;

/**
 * @file SprintCreationResponse.java
 * @brief Définition de la classe SprintCreationResponse.
 * @version 1.0
 * @date 01/04/2024
 */
@Getter
@Setter
public class SprintCreationResponse {

        // Attributs
        private List<Sprint> createdSprints;
        private List<String> errors;
}
