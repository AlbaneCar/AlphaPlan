package fr.eseo.backendalphaplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.eseo.backendalphaplan.model.Role;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @file RoleRepository.java
 * @brief Interface repository pour les Role
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * @brief Requête pour récupérer un rôle en fonction de son nom
     * @param nom : nom du rôle
     * @return Role
     */
    Optional<Role> findByNom(String nom);

    /**
     * @brief Requête pour récupérer les rôles d'un utilisateur
     * @param ids : liste des id des utilisateurs
     * @return Liste des rôles
     */
    @Query("SELECT r FROM Role r LEFT JOIN RoleUtilisateur ru on ru.role.id = r.id WHERE ru.utilisateur.id IN (:ids)")
    List<Role> findRoles(@Param("ids") List<Integer> ids);

    /**
     * @brief Requête pour récupérer les rôles en fonction de leur type
     * @param type : type du rôle
     * @return Role
     */
    @Query("SELECT r FROM Role r WHERE r.nom = ?1")
    Role findRoleByType(String type);
}

