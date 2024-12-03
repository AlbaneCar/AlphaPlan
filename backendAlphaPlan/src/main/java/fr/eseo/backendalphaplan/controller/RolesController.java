package fr.eseo.backendalphaplan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import fr.eseo.backendalphaplan.model.Role;
import fr.eseo.backendalphaplan.model.RoleUtilisateur;
import fr.eseo.backendalphaplan.services.RoleService;
import fr.eseo.backendalphaplan.services.RoleUtilisateurService;

/**
 * @file RolesController.java
 * @brief Classe controller de la gestion des roles
 * @version 1.0
 * @autor Hugo BOURDAIS
 * @date 24/04/2024
 */
@RestController
@RequestMapping("/api/roles")
public class RolesController {

    // Attributs
    private final RoleService roleService;
    private final RoleUtilisateurService roleUtilisateurService;

    // Constructeur
    @Autowired
    public RolesController(RoleService roleService, RoleUtilisateurService roleUtilisateurService) {
        this.roleService = roleService;
        this.roleUtilisateurService = roleUtilisateurService;
    }

    /**
     * @brief Récupère la liste des roles
     * @return List<Role> : liste des roles
     */
    @GetMapping
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    /**
     * @brief Récupère un role par les étudiants
     * @param ids : id des étudiants
     * @return Role : role
     */
    @GetMapping("/{ids}")
    public List<Role> getRolesByStudents(@PathVariable List<Integer> ids) {
        return roleService.getRolesByStudents(ids);
    }

    /**
     * @brief Récupère la liste des roles attribués
     * @return Role : role
     */
    @GetMapping("/attribue")
    public List<RoleUtilisateur> getRoleUtilisateur() {
        return roleUtilisateurService.getRoleUtilisateur();
    }

    /**
     * @brief Attribue un role à un utilisateur
     * @param userId : id de l'utilisateur
     * @param roleId : id du role
     * @return List<RoleUtilisateur> : liste des roles attribués
     */
    @PostMapping("/attribue/{userId}/{roleId}")
    public List<RoleUtilisateur> setRoleUtilisateur(@PathVariable Integer userId, @PathVariable Integer roleId) {
        return roleUtilisateurService.setRoleUtilisateur(userId, roleId);
    }
}
