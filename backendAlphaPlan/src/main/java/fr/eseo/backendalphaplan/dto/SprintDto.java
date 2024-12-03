package fr.eseo.backendalphaplan.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @file SprintDto.java
 * @brief Définition de la classe SprintDto.
 * @version 1.0
 * @date 01/04/2024
 */
@Getter
@Setter
public class SprintDto {

    // Attributs
    private LocalDate startDate;
    private LocalDate endDate;
    private String sprintEndType;
}
