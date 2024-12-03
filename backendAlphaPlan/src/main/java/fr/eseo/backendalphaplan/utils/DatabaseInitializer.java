package fr.eseo.backendalphaplan.utils;

import fr.eseo.backendalphaplan.model.*;
import fr.eseo.backendalphaplan.model.enums.Genre;
import fr.eseo.backendalphaplan.model.enums.RoleType;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;

import fr.eseo.backendalphaplan.repository.*;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @file DatabaseInitializer.java
 * @brief Initialisation de la base de données
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements ApplicationRunner {

    // Attributs
    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Constantes
    private static final String MAIL_SUFFIX = "@mail.com";
    private static final String HOMME = "HOMME";
    private static final String FEMME = "FEMME";
    private static final String ADMIN = "admin";
    private static final List<List<String>> SUPERVISING_STAFF = List.of(
            List.of("ROUSSEAU", "Sophie", FEMME),
            List.of("FERRET-CANAPE", "Clive", HOMME),
            List.of("ABDALLAH", "Maïssa", FEMME),
            List.of("CAMP", "Olivier", HOMME),
            List.of("CHHEL", "François", HOMME),
            List.of("CLAVREUL", "Mickaël", HOMME)
    );

    /**
     * @brief Initialisation de la base de données
     * @param args : Arguments de l'application
     */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        addRoles();
        if(!adminExists()) {
            createAdmin();
        }
        for(int i = 0; i < SUPERVISING_STAFF.size(); i++) {
            if(!supervisingStaffExists(i)) {
                createSupervisingStaff(i);
            }
        }
    }

    /**
     * @brief Créer un administrateur
     */
    private void createAdmin() {
        Utilisateur admin = new Utilisateur();
        admin.setNom(ADMIN);
        admin.setPrenom(ADMIN);
        admin.setEmail(ADMIN + MAIL_SUFFIX);
        admin.setMotDePasse(passwordEncoder.encode(ADMIN)); //NOSONAR
        admin.setGenre(Genre.AUTRE);
        admin.setTypeUtilisateur(TypeUtilisateur.ENCADRANT);
        List<RoleUtilisateur> roles = new ArrayList<>();
        roles.add(new RoleUtilisateur(admin, getRole("PROJECT_LEADER")));
        admin.setRoles(roles);
        utilisateurRepository.save(admin);

    }

    private void createSupervisingStaff(int i) {
        String name = SUPERVISING_STAFF.get(i).get(0) + " " + SUPERVISING_STAFF.get(i).get(1);
        String mail = UtilsGenerator.generateMail(name);
        String username = UtilsGenerator.generateUsername(name);
        Utilisateur supervisingStaff = new Utilisateur();
        supervisingStaff.setNom(SUPERVISING_STAFF.get(i).get(0));
        supervisingStaff.setPrenom(SUPERVISING_STAFF.get(i).get(1));
        supervisingStaff.setEmail(mail);
        supervisingStaff.setMotDePasse(passwordEncoder.encode(username)); //NOSONAR
        supervisingStaff.setGenre(Genre.getGenre(SUPERVISING_STAFF.get(i).get(2)));
        supervisingStaff.setTypeUtilisateur(TypeUtilisateur.ENCADRANT);
        List<RoleUtilisateur> roles = new ArrayList<>();
        roles.add(new RoleUtilisateur(supervisingStaff, getRole("SUPERVISING_STAFF")));
        supervisingStaff.setRoles(roles);
        utilisateurRepository.save(supervisingStaff);
    }


    /**
     * @brief Ajouter les rôles
     */
    private void addRoles() {
        for(RoleType roleType : RoleType.values()) {
            if(!roleExists(roleType)) {
                Role role = new Role();
                role.setNom(roleType.getNom());
                role.setDescription(roleType.getDescription());
                roleRepository.save(role);
            }
        }
    }

    /**
     * @brief Vérifier si l'administrateur existe
     * @return boolean
     */
    private boolean adminExists() {
        return utilisateurRepository.findOptionalByEmail("admin@mail.com").isPresent();
    }

    private boolean supervisingStaffExists(int i) {
        String mail = UtilsGenerator.generateMail(SUPERVISING_STAFF.get(i).get(0) + " " + SUPERVISING_STAFF.get(i).get(1));
        return utilisateurRepository.findOptionalByEmail(mail).isPresent();
    }



    /**
     * @brief Vérifier si le rôle existe
     * @param roleType : Type de rôle
     * @return boolean
     */
    private boolean roleExists(RoleType roleType) {
        return roleRepository.findByNom(roleType.getNom()).isPresent();
    }

    /**
     * @brief Récupérer le rôle
     * @return Role
     */
    private Role getRole(String role) {
        return roleRepository.findByNom(role)
                .orElseThrow(() -> new NoSuchElementException("Role not found"));
    }
}
