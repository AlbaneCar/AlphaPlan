package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.dto.SprintCreationResponse;
import fr.eseo.backendalphaplan.dto.SprintDto;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.services.SprintService;

import fr.eseo.backendalphaplan.utils.Note;
import fr.eseo.backendalphaplan.utils.NotesUtilisateur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @file SprintController.java
 * @brief Définition de la classe SprintController.
 * @version 1.0
 * @date 01/04/2024
 */
@RestController
@RequestMapping("/api/sprints")
public class SprintController {

    // Attribut
    private final SprintService sprintService;

    // Constructeur
    @Autowired
    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    /**
     * @brief Méthode permettant de récupérer tous les sprints.
     * @return List<Sprint> : Liste des sprints.
     * @date 01/04/2024
     */
    @GetMapping
    public List<Sprint> getAllSprints() {
        return sprintService.getAllSprints();
    }

    /**
     * @brief Méthode permettant de récupérer un sprint par son id.
     * @param id : Integer : Id du sprint à récupérer.
     * @return ResponseEntity<Sprint> : Sprint récupéré.
     * @date 01/04/2024
     */
    @GetMapping("/{id}")
    public ResponseEntity<Sprint> getSprintById(@PathVariable Integer id) {
        Optional<Sprint> sprint = sprintService.getSprintById(id);
        return sprint.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * @brief Méthode permettant de créer des sprints.
     * @param sprints : List<Sprint> : Liste des sprints à créer.
     * @return ResponseEntity<List<Sprint>> : Liste des sprints créés.
     * @date 01/04/2024
     */
    @PostMapping
    public ResponseEntity<List<Sprint>> createSprints(@RequestBody List<Sprint> sprints) {
        List<Sprint> createdSprints = sprintService.saveSprints(sprints);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSprints);
    }

    /**
     * @brief Méthode permettant de créer des sprints à partir de données de formulaire.
     * @param sprintDtos : List<SprintDto> : Liste des données de formulaire.
     * @return ResponseEntity<SprintCreationResponse> : Réponse de création des sprints.
     * @date 01/04/2024
     */
    @PreAuthorize("hasAnyAuthority('optionLeader:write', 'projectLeader:write')")
    @PostMapping("/form")
    public ResponseEntity<SprintCreationResponse> createSprintsFromFormData(@RequestBody List<SprintDto> sprintDtos) {

        sprintService.deleteAllSprints();

        List<Sprint> sprints = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < sprintDtos.size(); i++) {
            SprintDto sprintDto = sprintDtos.get(i);
            Sprint sprint = new Sprint();
            sprint.setStartDate(sprintDto.getStartDate());
            sprint.setEndDate(sprintDto.getEndDate());
            sprint.setName("Sprint " + (i + 1));

            // Convertir le champ String en valeur d'énumération
            sprint.setSprintEndType(Sprint.convertStringToEnum(sprintDto.getSprintEndType()));

            // Valider les dates pour chaque sprint
            if (sprint.getStartDate().isBefore(LocalDate.now())) {
                errors.add("La date de début du Sprint " + (i + 1) + " ne peut pas être antérieure à la date actuelle.");
            }

            if (sprint.getEndDate().isBefore(sprint.getStartDate())) {
                errors.add("La date de fin du Sprint " + (i + 1) + " ne peut pas être antérieure à la date de début.");
            }

            if (i > 0) {
                Sprint previousSprint = sprints.get(i - 1);
                if (sprint.getStartDate().isBefore(previousSprint.getEndDate())) {
                    errors.add("La date de début du Sprint " + (i + 1) + " ne peut pas être antérieure à la date de fin du Sprint " + i + ".");
                }
            }

            sprints.add(sprint);

        }

        SprintCreationResponse response = new SprintCreationResponse();
        response.setCreatedSprints(sprints);
        response.setErrors(errors);

        // Vérifier s'il y a des erreurs de validation
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(response);
        }

        sprintService.saveSprints(sprints);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * @brief Méthode permettant de supprimer un sprint.
     * @param id : Integer : Id du sprint à supprimer.
     * @return ResponseEntity<Void> : Réponse de suppression.
     * @date 01/04/2024
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Integer id) {
        sprintService.deleteSprint(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * @brief Méthode permettant de récupérer le nombre de sprints.
     * @return ResponseEntity<Integer> : Nombre de sprints.
     * @autor Hugo BOURDAIS
     * @date 01/04/2024
     */
    @GetMapping("/nombre")
    public ResponseEntity<Integer> getNombreSprints() {
        return ResponseEntity.ok().body(sprintService.getAllSprints().size());
    }

    /**
     * @brief Méthode permettant de récupérer tous les sprints.
     * @return ResponseEntity<List<Sprint>> : Liste des sprints.
     * @autor Hugo BOURDAIS
     * @date 01/04/2024
     */
    @GetMapping("/")
    public ResponseEntity<List<Sprint>> getSprints() {
        return ResponseEntity.ok().body(sprintService.getAllSprints());
    }

    /**
     * @brief Méthode permettant de récupérer les notes de l'élève pour un sprint donné.
     * @param eleveId : Integer : Id de l'élève.
     * @param sprintId : Integer : Id du sprint.
     * @return ResponseEntity<List<Note>> : Liste des notes de l'élève pour le sprint donné.
     * @autor Hugo BOURDAIS
     * @date 01/04/2024
     */
    @GetMapping("/notes/{eleveId}/{sprintId}")
    public ResponseEntity<List<Note>> getNotesEleveBySprintId(@PathVariable Integer eleveId, @PathVariable Integer sprintId) {
        try {
            // Récupérer les notes de l'élève pour le sprint donné
            return ResponseEntity.ok().body(sprintService.getNoteEleveByEleveAndSprint(eleveId, sprintId));
        } catch (IllegalArgumentException e) {
            // Retourner une réponse 400 Bad Request avec un message d'erreur
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @brief Méthode permettant de récupérer les notes de l'équipe pour un sprint donné.
     * @param equipeId : Integer : Id de l'équipe.
     * @param sprintId : Integer : Id du sprint.
     * @return ResponseEntity<List<NotesUtilisateur>> : Liste des notes de l'équipe pour le sprint donné.
     * @autor Hugo BOURDAIS
     * @date 01/04/2024
     */
    @GetMapping("/notes_equipe/{equipeId}/{sprintId}")
    public ResponseEntity<List<NotesUtilisateur>> getNotesEquipeBySprintId(@PathVariable Integer equipeId, @PathVariable Integer sprintId) {
        try {
            // Récupérer les notes de l'équipe pour le sprint donné
            return ResponseEntity.ok().body(sprintService.getNoteEquipeByEquipeAndSprint(equipeId, sprintId));
        } catch (IllegalArgumentException e) {
            // Retourner une réponse 400 Bad Request avec un message d'erreur
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @brief Méthode permettant de mettre à jour un sprint.
     * @param sprint : Sprint : Sprint à mettre à jour.
     * @return ResponseEntity<Sprint> : Sprint mis à jour.
     * @autor Hugo BOURDAIS
     * @date 01/04/2024
     */
    @PostMapping("/update")
    public ResponseEntity<Sprint> updateSprint(@RequestBody Sprint sprint) {
        // On regarde si le sprint à mettre à jour existe
        if (sprintService.getSprintById(sprint.getId()).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Sprint updatedSprint = sprintService.updateSprint(sprint);
        return ResponseEntity.ok(updatedSprint);
    }
}
