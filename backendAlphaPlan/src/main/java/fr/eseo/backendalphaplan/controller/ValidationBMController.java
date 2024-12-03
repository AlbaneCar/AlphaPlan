package fr.eseo.backendalphaplan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fr.eseo.backendalphaplan.model.ValidationBM;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import fr.eseo.backendalphaplan.services.ValidationBMService;

/**
 * @file ValidationBMController.java
 * @brief Définition de la classe ValidationBMController.
 * @version 1.0
 * @author Yann
 * @date 24/04/2024
 */
@RestController
@RequestMapping("/api/v1.0/validationbm")
public class ValidationBMController {

    // Attributs
    private static final Logger logger = LoggerFactory.getLogger(ValidationBMController.class);
    private final ValidationBMService validationBMService;

    // Constructeur
    @Autowired
    public ValidationBMController(ValidationBMService validationBMService) {
        this.validationBMService = validationBMService;
    }
	
    /**
     * @brief Méthode permettant d'ajouter une validation sur un BonusMalus
     * @return Ajout de la validation par l'élève du BM
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @PostMapping("/ajouter")
    public ResponseEntity<ValidationBM> ajouterValidation(@RequestBody ValidationBM validationBM) {
        try {
            ValidationBM v = validationBMService.saveValidationBM(validationBM);
            if (v == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(v);
        } catch (Exception e) {
            logger.error("An error occurred: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * @brief Méthode permettant d'avoir le nombre de validations d'un utilisateur sur un sprint
     * @return Nombre de BM validés par l'utilisateur
     * @autor Yann LIDEC
     * @date 04/05/2024
     */
    @GetMapping("/{id}/{type}")
    public int getNbValidationsBM(@PathVariable Integer id, @PathVariable TypeNoteEleve type, @RequestParam int sprint, @RequestParam int equipe) {
        return validationBMService.getNbValidationsBM(id, type, sprint, equipe);
    }
}
