package fr.eseo.backendalphaplan.services;

import java.util.List;

import fr.eseo.backendalphaplan.model.Role;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.RoleRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eseo.backendalphaplan.model.RoleUtilisateur;
import fr.eseo.backendalphaplan.repository.RoleUtilisateurRepository;

import jakarta.transaction.Transactional;

/**
 * @file RoleUtilisateurService.java
 * @brief Service pour les RoleUtilisateur
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Transactional
@Service
public class RoleUtilisateurService {

    // RoleUtilisateurRepository
    private final RoleUtilisateurRepository roleUtilisateurRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;

    // Constructeur
    @Autowired
    public RoleUtilisateurService(RoleUtilisateurRepository roleUtilisateurRepository, UtilisateurRepository utilisateurRepository, RoleRepository roleRepository) {
        this.roleUtilisateurRepository = roleUtilisateurRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * @brief Récupérer tous les RoleUtilisateur
     * @return Liste des RoleUtilisateur
     */
    public List<RoleUtilisateur> getRoleUtilisateur() {
        return roleUtilisateurRepository.findAllWithUtilisateur();
    }

    /**
     * @brief Supprimer un RoleUtilisateur
     * @param roleUtilisateur : RoleUtilisateur à supprimer
     */
    public void deleteRoleUtilisateur(RoleUtilisateur roleUtilisateur) {
        roleUtilisateurRepository.delete(roleUtilisateur);
    }

    /**
     * @brief Sauvegarder un RoleUtilisateur
     * @param roleUtilisateur : RoleUtilisateur à sauvegarder
     */
    public void saveRoleUtilisateur(RoleUtilisateur roleUtilisateur) {
        roleUtilisateurRepository.save(roleUtilisateur);
    }

    /**
     * @brief Enregistrer un RoleUtilisateur pour un utilisateur
     * @param userId : ID de l'utilisateur
     * @param roleId : ID du rôle
     * @return Liste des rôles de l'utilisateur
     */
    @Transactional
    public List<RoleUtilisateur> setRoleUtilisateur(Integer userId, Integer roleId) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rôle non trouvé avec l'ID : " + roleId));

        RoleUtilisateur roleUtilisateur = new RoleUtilisateur(utilisateur, role);
        roleUtilisateurRepository.save(roleUtilisateur);

        return utilisateur.getRoles();
    }
}
