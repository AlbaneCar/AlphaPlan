package fr.eseo.backendalphaplan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.services.UtilisateurService;

/**
 * @file EtudiantController.java
 * @brief La classe EtudiantController est le controller qui permet de gérer les requêtes HTTP concernant les étudiants
 * @version 1.0
 */
@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {

    // Attributs
    private final UtilisateurService utilisateurService;

    // Constructeur
    @Autowired
    public EtudiantController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    /**
     * @brief Méthode permettant de récupérer tous les étudiants
     * @return Liste des étudiants
     */
    @GetMapping
    public List<Utilisateur> getStudents() {
        return utilisateurService.getUtilisateurs();
    }

    /**
     * @brief Méthode permettant de récupérer un étudiant par son id
     * @param id : id de l'étudiant
     * @return Utilisateur
     */
    @GetMapping("/{id}")
    public Utilisateur getUserById(@PathVariable Integer id) {
        return utilisateurService.getUtilisateurById(id);
    }

    /**
     * @brief Méthode permettant de récupérer tous les étudiants
     * @return Liste des étudiants
     */
    @GetMapping("/eleves")
    public List<Utilisateur> getEtudiants() {
        return utilisateurService.getEleves();
    }

    /**
     * @brief Méthode permettant de récupérer tous les enseignants
     * @return Liste des enseignants
     */
    @GetMapping("/teachers")
    public List<Utilisateur> getTeachers() {
        return utilisateurService.getUtilisateursEncadrant();
    }

    /**
     * @brief Méthode permettant d'ajouter un étudiant
     * @param user : étudiant à ajouter
     * @return Utilisateur
     */
    @PostMapping("/ajouter")
    public ResponseEntity<Utilisateur> addStudent(@RequestBody Utilisateur user) {
        Utilisateur existingUser = utilisateurService.getUtilisateurById(user.getId());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(existingUser);
        }
        Utilisateur newUser = utilisateurService.saveUtilisateur(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * @brief Méthode permettant de supprimer un étudiant
     * @param id : id de l'étudiant à supprimer
     * @return Validation de la suppression de l'étudiant
     */
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
        	utilisateurService.deleteUserById(id);
            return ResponseEntity.ok().body("L'utilisateur a été supprimé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur lors de la suppression de l'utilisateur.");
        }
    }
}
