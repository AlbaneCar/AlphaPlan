package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.ValidationEquipes;
import fr.eseo.backendalphaplan.model.enums.EtatEquipes;
import fr.eseo.backendalphaplan.services.EquipeService;
import fr.eseo.backendalphaplan.services.ValidationEquipesService;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @file ValidationEquipesController.java
 * @brief Définition de la classe ValidationEquipesController.
 * @version 1.0
 * @author Yann
 * @date 24/04/2024
 */
@RestController
@RequestMapping("/api/v1.0/validationequipes")
public class ValidationEquipesController {

    // Attributs
    private final ValidationEquipesService validationEquipesService;
    private final EquipeService equipeService;

    // Constructeur
    @Autowired
    public ValidationEquipesController(ValidationEquipesService validationEquipesService, EquipeService equipeService) {
        this.validationEquipesService = validationEquipesService;
        this.equipeService = equipeService;
    }

    /**
     * @brief Méthode permettant de récupérer toutes les validations des équipes.
     * @return List<ValidationEquipes> : Liste des validations des équipes récupérées.
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @GetMapping
    public ResponseEntity<List<ValidationEquipes>> getAllValidationEquipes() {
        List<ValidationEquipes> validationEquipesList = validationEquipesService.getAllValidationEquipes();
        return ResponseEntity.ok(validationEquipesList);
    }

    /**
     * @brief Méthode permettant de créer une validation d'équipe.
     * @return ValidationEquipes : Validation d'équipe créée.
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @PostMapping("/{utilisateurId}")
    public ResponseEntity<ValidationEquipes> createValidationEquipe(@PathVariable Integer utilisateurId) {
        try {
            ValidationEquipes validationEquipes = validationEquipesService.createValidationEquipe(utilisateurId);
            return ResponseEntity.ok(validationEquipes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * @brief Méthode permettant de supprimer une validation d'équipe.
     * @return ValidationEquipes : Validation d'équipe supprimée.
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllValidationEquipes() {
        validationEquipesService.deleteAllValidationEquipes();
        equipeService.changeTeamsState(EtatEquipes.GENERE);
        return ResponseEntity.noContent().build();
    }
}
