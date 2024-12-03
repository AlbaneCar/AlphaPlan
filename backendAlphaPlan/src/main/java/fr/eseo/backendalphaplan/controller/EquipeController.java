package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.services.EquipeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @file EquipeController.java
 * @brief La classe EquipeController est le controller qui permet de gérer les requêtes HTTP concernant les équipes
 * @version 1.0
 * @autor Yann LIDEC
 * @date 24/05/2024
 */
@RestController
@RequestMapping("/api/v1.0/equipes")
public class EquipeController {

    // Attributs
    private final EquipeService equipeService;

    // Constructeur
    @Autowired
    public EquipeController(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    /**
     * @brief Méthode permettant de créer une équipe
     * @param equipe : équipe à ajouter
     * @return Equipe ajoutée
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @PostMapping
    public Equipe createEquipe(@RequestBody Equipe equipe) {
        return equipeService.saveEquipe(equipe);
    }

    /**
     * @brief Méthode permettant de récupérer une équipe par son id
     * @param id : id de l'équipe à récupérer
     * @return Equipe récupérée
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @GetMapping("/{id}")
    public ResponseEntity<Equipe> getEquipeById(@PathVariable Integer id) {
        Equipe equipe = equipeService.getEquipeById(id);
        if (equipe != null) {
            return ResponseEntity.ok(equipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * @brief Méthode permettant de récupérer toutes les équipes
     * @return Liste des équipes
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @GetMapping
    public List<Equipe> getAllEquipes() {
        return equipeService.getAllEquipes();
    }

    /**
     * @brief Méthode permettant de mettre à jour une équipe
     * @param id : id de l'équipe à mettre à jour
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @PutMapping("/{id}")
    public ResponseEntity<Equipe> updateEquipe(@PathVariable Integer id, @RequestBody Equipe equipeDetails) {
        Equipe equipe = equipeService.getEquipeById(id);
        if (equipe != null) {
            equipe.setNom(equipeDetails.getNom());
            // Update other fields as needed
            return ResponseEntity.ok(equipeService.saveEquipe(equipe));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * @brief Méthode permettant de supprimer les équipes
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @DeleteMapping("/supprimer")
    public ResponseEntity<Void> supprimerEquipes() {
        equipeService.removeAllEquipes();
        return ResponseEntity.ok().build();
    }
}