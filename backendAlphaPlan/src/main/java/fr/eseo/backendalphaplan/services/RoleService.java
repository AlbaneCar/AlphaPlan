package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Role;
import fr.eseo.backendalphaplan.repository.RoleRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @file RoleService.java
 * @brief Service pour les rôles
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
public class RoleService {
    
    // Attributs
    private final RoleRepository roleRepository;

    // Constructeur
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * @brief Récupérer tous les rôles
     * @return Liste des rôles
     */
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    /**
     * @brief Récupérer un rôle via les id des étudiants
     * @param ids : Liste des id des étudiants
     * @return Liste des rôles
     */
    public List<Role> getRolesByStudents(List<Integer> ids) {
        return roleRepository.findRoles(ids);
    }

    /**
     * @brief Récupérer un rôle via son type
     * @param type : Type du rôle
     * @return Rôle
     */
    public Role getRoleByType(String type) {
        return roleRepository.findRoleByType(type);
    }
}
