package fr.eseo.backendalphaplan.model.id;

import lombok.Data;

import java.io.Serializable;

/**
 * @file UtilisateurRoleId.java
 * @brief Classe permettant de définir la clé primaire de la table UtilisateurRole
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Data
public class UtilisateurRoleId implements Serializable {

	// Attributs
	private static final long serialVersionUID = 1L;
	private Integer utilisateur;
    private Integer role;
}
