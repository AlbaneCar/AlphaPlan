package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.services.OrdrePassageService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @file OrdrePassageController.java
 * @brief Controller permettant de gérer les ordres de passage
 * @details Ce controller permet de récupérer un ordre de passage en fonction de l'id de l'équipe et de l'id du sprint, et de créer un ordre de passage
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 24/05/2024
 */
@RestController
@RequestMapping("/api/v1.0/ordrePassage")
public class OrdrePassageController {

    // Attributs
    private final OrdrePassageService ordrePassageService;

    // Constructeur
    public OrdrePassageController(OrdrePassageService ordrePassageService) {
        this.ordrePassageService = ordrePassageService;
    }

    /**
     * @brief Récupère l'ordre de passage en fonction de l'id de l'équipe et de l'id du sprint
     * @details Cette méthode permet de récupérer un ordre de passage en fonction de l'id du sprint et de l'id de l'équipe
     * @param sprintId l'id du sprint
     * @param equipeId l'id de l'équipe
     * @return une réponse
     * @throws IllegalArgumentException si l'id du sprint ou l'id de l'équipe ne sont pas renseignés, si l'équipe n'existe pas, si le sprint n'existe pas ou si l'ordre de passage n'existe pas
     * @date 24/05/2024
     * @autor Hugo BOURDAIS
     */
    @GetMapping("/{sprintId}/{equipeId}")
    public ResponseEntity<OrdrePassageResponse> getOrdrePassageByEquipeIdAndSprintId(@PathVariable Integer sprintId, @PathVariable Integer equipeId) {
        try {
            return ResponseEntity.ok(ordrePassageService.getOrdrePassageByEquipeIdAndSprintId(sprintId, equipeId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(OrdrePassageResponse.builder().error(e.getMessage()).build());
        }
    }

    /**
     * @brief Crée un ordre de passage
     * @details Cette méthode permet de créer un ordre de passage en fonction de l'id du sprint, de l'id de l'équipe, de l'ordre de passage et de l'id de l'auteur
     * @param sprintId l'id du sprint
     * @param equipeId l'id de l'équipe
     * @param ordre l'ordre de passage
     * @param auteurId l'id de l'auteur
     * @return une réponse
     * @throws IllegalArgumentException si l'id du sprint ou l'id de l'équipe ne sont pas renseignés, si l'équipe n'existe pas, si le sprint n'existe pas ou si l'ordre de passage n'existe pas
     * @date 24/05/2024
     * @autor Hugo BOURDAIS
     */
    @PostMapping("/{sprintId}/{equipeId}")
    public ResponseEntity<String> createOrdrePassage(@PathVariable Integer sprintId, @PathVariable Integer equipeId, @RequestParam List<Integer> ordre, @RequestParam Integer auteurId) {
        try {
            ordrePassageService.createOrdrePassage(sprintId, equipeId, ordre, auteurId);
            return ResponseEntity.ok("Ordre de passage enregistré");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * @brief Réponse renvoyée par le controller
     * @details Cette classe permet de renvoyer une réponse suite à une requête
     * @author Hugo BOURDAIS
     * @date 24/05/2024
     */
    @Data
    @AllArgsConstructor
    @Builder
    @RequiredArgsConstructor
    public static final class OrdrePassageResponse {
        private Integer id;
        private Equipe equipe;
        private Sprint sprint;
        private Utilisateur auteur;
        private String dateCreation;
        private List<Utilisateur> ordre;
        private String error;
    }
}
