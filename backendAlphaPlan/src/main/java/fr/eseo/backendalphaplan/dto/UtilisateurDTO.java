package fr.eseo.backendalphaplan.dto;

import fr.eseo.backendalphaplan.model.enums.Genre;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;
import lombok.Data;

/**
 * @file UtilisateurDTO.java
 * @brief Définition de la classe UtilisateurDTO.
 * @version 1.0
 * @date 01/04/2024
 */
@Data
public class UtilisateurDTO {

    // Attributs
    private Integer id;
    private String nom;
    private String prenom;
    private Double moyenne;
    private Genre genre;
    private TypeUtilisateur typeUtilisateur;
}