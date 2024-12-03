package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.RoleUtilisateur;

import fr.eseo.backendalphaplan.model.id.UtilisateurRoleId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @file RoleUtilisateurRepository.java
 * @brief Interface repository pour les RoleUtilisateur
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface RoleUtilisateurRepository extends JpaRepository<RoleUtilisateur, UtilisateurRoleId> {

	/**
	 * @brief Requête pour récupérer tous les rôles d'un utilisateur
	 * @return Liste des RoleUtilisateur
	 */
	@Query("SELECT ru FROM RoleUtilisateur ru")
	List<RoleUtilisateur> findAllWithUtilisateur();

	/**
	 * @brief Requête pour récupérer tous les rôles d'un utilisateur via son id
	 * @param id : id de l'utilisateur
	 * @return Liste des RoleUtilisateur
	 */
	@Query("SELECT ru FROM RoleUtilisateur ru WHERE ru.utilisateur.id = ?1")
	List<RoleUtilisateur> findAllByUtilisateurId(Integer id);
}
