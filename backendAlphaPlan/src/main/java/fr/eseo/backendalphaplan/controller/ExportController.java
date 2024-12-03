package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.services.ExportNotesService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @file NotesController.java
 * @brief Définition de la classe NotesController.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 01/04/2024
 */
@RestController // Définition de la classe comme contrôleur
@RequestMapping("/api/export") // Définition de la route
public class ExportController {

    // Attributs
    private final ExportNotesService exportNotesServices;

    // Constructeur
    @Autowired
    public ExportController(ExportNotesService exportNotesServices) {
        this.exportNotesServices = exportNotesServices;
    }

    /**
     * @return ResponseEntity<String> : Réponse HTTP contenant le fichier CSV.
     * @brief Méthode permettant d'exporter les notes des étudiants en format CSV.
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    @GetMapping("/notes-sprints")
    public ResponseEntity<String> exportNotesSprintsCSV(HttpServletResponse response) {
        try {
            exportNotesServices.getXSLX(response);
            return ResponseEntity.ok().body("Exportation des notes des étudiants en format XLSX réussie.");
        } catch (IOException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
