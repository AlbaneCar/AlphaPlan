package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @file UtilisateurRepository.java
 * @brief Interface repository pour les Utilisateur
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    /**
     * @brief Requête pour récupérer les utilisateurs de type E3E ou BACHELOR
     * @param pageable : pagination
     * @return Page des utilisateurs
     */
    @Query("SELECT u FROM Utilisateur u WHERE u.typeUtilisateur = 'E3E' OR u.typeUtilisateur = 'BACHELOR'")
    Page<Utilisateur> findAllE3EOrBachelorUsers(Pageable pageable);

    /**
     * @brief Requête pour récupérer les utilisateurs de type E3E ou BACHELOR
     * @return Liste des utilisateurs
     */
    @Query("SELECT u FROM Utilisateur u WHERE u.email = :email")
    Utilisateur findByEmail(@Param("email") String email);

    /**
     * @brief Requête pour récupérer les utilisateurs de type E3E ou BACHELOR
     * @return Liste des utilisateurs
     */
    @Query("SELECT u FROM Utilisateur u WHERE u.nom = :nom AND u.prenom = :prenom")
    Utilisateur findNomAndPrenom(String nom, String prenom);

    /**
     * @brief Requête pour récupérer les utilisateurs de type E3E ou BACHELOR
     * @return Liste des utilisateurs
     */
    @Query("SELECT u FROM Utilisateur u WHERE u.typeUtilisateur = :type")
    List<Utilisateur> findEncadrant(TypeUtilisateur type);

    /**
     * @brief Requête pour récupérer les utilisateurs de type E3E ou BACHELOR
     * @return Liste des utilisateurs
     */
    @Query("SELECT u FROM Utilisateur u WHERE u.typeUtilisateur = :type OR u.typeUtilisateur = :type2")
    List<Utilisateur> findEleve(TypeUtilisateur type, TypeUtilisateur type2);

    /**
     * @brief Requête pour récupérer les utilisateurs de type E3E ou BACHELOR
     * @return Liste des utilisateurs
     */
    @Query("SELECT u FROM Utilisateur u WHERE u.equipe = :equipe")
    List<Utilisateur> findByEquipe(Equipe equipe);

    /**
     * @brief Requête pour récupérer les utilisateurs de type E3E ou BACHELOR
     * @return Liste des utilisateurs
     */
    @Query("SELECT u FROM Utilisateur u WHERE u.email = :email")
    Optional<Utilisateur> findOptionalByEmail(String email);

    /**
     * @brief Requête pour récupérer les utilisateurs de type E3E ou BACHELOR
     * @return Liste des utilisateurs
     */
    @Query("SELECT u FROM Utilisateur u WHERE u.typeUtilisateur = 'ENCADRANT'")
    List<Utilisateur> findEncadrants();

    /**
     * @brief Requête pour récupérer les utilisateurs de type E3E ou BACHELOR
     * @return Liste des utilisateurs
     */
    @Query("SELECT u FROM Utilisateur u JOIN u.roles ur JOIN ur.role r WHERE r.nom = 'OPTION_LEADER' OR r.nom = 'PROJECT_LEADER'")
    List<Utilisateur> findOLPL();
}
