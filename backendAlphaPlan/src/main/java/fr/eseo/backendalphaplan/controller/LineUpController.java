package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.LineUp;
import fr.eseo.backendalphaplan.model.ValidationLineUp;
import fr.eseo.backendalphaplan.services.LineUpService;
import fr.eseo.backendalphaplan.services.ValidationLineUpService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @file LineUpController.java
 * @brief Définition de la classe LineUpController.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 09/06/2024
 */
@RestController // Définition de la classe comme contrôleur
@RequestMapping("/api/lineUp") // Définition de la route
public class LineUpController {

    // Attribut
    private final ValidationLineUpService validationLineUpService;
    private final LineUpService lineUpService;

    // Constructeur
    @Autowired
    public LineUpController(ValidationLineUpService validationLineUpService, LineUpService lineUpService) {
        this.lineUpService = lineUpService;
        this.validationLineUpService = validationLineUpService;
    }

    /**
     * @brief Méthode permettant de créer une propostion de line-up.
     * @param req : {@link LineUpRequest}
     * @return ResponseEntity : {@link LineUpResponse}
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    @PostMapping("/creer")
    public ResponseEntity<LineUpResponse> createLineUpProposition(@RequestBody LineUpRequest req) {
        try {
            LineUp lineUp = lineUpService.createLineUp(req);
            return ResponseEntity.ok(LineUpResponse.builder().message("Proposition enregistrée avec succès !").lineUp(lineUp).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(LineUpResponse.builder().message(e.getMessage()).build());
        }
    }

    /**
     * @brief Méthode permettant de créer une propostion de line-up.
     * @param req : {@link LineUpRequest}
     * @return ResponseEntity : {@link LineUpResponse}
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    @PostMapping("/history")
    public ResponseEntity<LineUpResponse> checkIfAlreadyCreated(@RequestBody LineUpRequest req) {
        try {
            Integer auteurId = req.getAuteur();
            List<LineUp> lineUps = lineUpService.getLineUpByAuteur(auteurId);
            return ResponseEntity.ok(LineUpResponse.builder().lineUps(lineUps).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(LineUpResponse.builder().message(e.getMessage()).build());
        }
    }

    /**
     * @brief Méthode permettant de récupérer toutes les propostions de line-up.
     * @return ResponseEntity : {@link LineUpResponse}
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    @GetMapping("/liste")
    public ResponseEntity<LineUpResponse> getAllLineUpPropositions() {
        List<LineUp> lineUps = lineUpService.getAll();
        return ResponseEntity.ok(LineUpResponse.builder().lineUps(lineUps).build());
    }

    /**
     * @brief Méthode permettant de valider ou non un changement de line-up
     * @param req {@link ValidationRequest}
     * @return ResponseEntity : {@link LineUpResponse}
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    @PostMapping("/update")
    public ResponseEntity<LineUpResponse> updateLineUp(@RequestBody ValidationRequest req) {
        try {
            String message = lineUpService.updateLineUp(req);
            return ResponseEntity.ok(LineUpResponse.builder().message(message).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(LineUpResponse.builder().message(e.getMessage()).build());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<LineUpResponse> deleteLineUp(@RequestBody LineUpRequest req) {
        try {
            String message = lineUpService.deleteLineUp(req);
            return ResponseEntity.ok(LineUpResponse.builder().message(message).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(LineUpResponse.builder().message(e.getMessage()).build());
        }
    }

    /**
     * @brief Méthode permettant de récupérer les status de validation d'une line-up.
     * @param validationRequest {@link ValidationRequest}
     * @return ResponseEntity {@link ValidationResponse}
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    @PostMapping("/validations/liste")
    public ResponseEntity<ValidationResponse> getLineUpStatus(@RequestBody ValidationRequest validationRequest) {
        try {
            Integer lineUpId = validationRequest.getProposition();
            List<ValidationLineUp> validations = validationLineUpService.getStatusOf(lineUpId);
            return ResponseEntity.ok(ValidationResponse.builder().validations(validations).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ValidationResponse.builder().message(e.getMessage()).build());
        }
    }

    /**
     * @brief Méthode permettant de mettre à jour la validation d'un membre quant à une propostion de line-up.
     * @param req : {@link ValidationRequest}
     * @return ResponseEntity : {@link ValidationRequest}
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    @PutMapping("/validations/update")
    public ResponseEntity<ValidationResponse> updateValidationSlot(@RequestBody ValidationRequest req) {
        try {
            validationLineUpService.analyseRequest(req);
            return ResponseEntity.ok(ValidationResponse.builder().message("Réponse soumise avec succès !").build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ValidationResponse.builder().message(e.getMessage()).build());
        }
    }

    /**
     * @brief Classe interne permettant de définir une requête de création de line-up.
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static final class LineUpRequest {
        private Integer auteur;
        private Integer proposition;
    }

    /**
     * @brief Classe interne permettant de définir une réponse création de line-up.
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Builder
    public static final class LineUpResponse {
        private List<LineUp> lineUps;
        private LineUp lineUp;
        private String message;
    }

    /**
     * @brief Classe interne permettant de définir une requête de validation.
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static final class ValidationRequest {
        private Integer auteur;
        private Integer proposition;
        private Boolean reponse;
    }

    /**
     * @brief Classe interne permettant de définir une réponse de validation.
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Builder
    public static final class ValidationResponse {
        private List<ValidationLineUp> validations;
        private String message;
    }
}
