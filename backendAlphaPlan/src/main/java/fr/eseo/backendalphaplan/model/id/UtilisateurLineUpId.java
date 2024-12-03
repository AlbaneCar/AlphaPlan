package fr.eseo.backendalphaplan.model.id;

import lombok.Data;

import java.io.Serializable;

/**
 * @file UtilisateurLineUpId.java
 * @brief Définition de la classe UtilisateurLineUpId.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 09/06/2024
 */
@Data
public class UtilisateurLineUpId implements Serializable {

    // Attributs
    private static final long serialVersionUID = 1L;
    private Integer utilisateur;
    private Integer lineUp;
}