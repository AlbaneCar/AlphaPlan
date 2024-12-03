package fr.eseo.backendalphaplan.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.eseo.backendalphaplan.model.enums.Matiere;
import fr.eseo.backendalphaplan.services.NoteAnterieureService;

/**
 * @file NoteAnterieureController.java
 * @brief Définition de la classe NoteAnterieureController.
 * @version 1.0
 */
@RestController
@RequestMapping("/api/notes")
public class NoteAnterieureController {

    // Attributs
    private final NoteAnterieureService noteAnterieureService;

    // Constructeur
    @Autowired
    public NoteAnterieureController(NoteAnterieureService noteAnterieureService) {
        this.noteAnterieureService = noteAnterieureService;
    }

    /**
     * @brief Méthode permettant de récupérer la moyenne de l'équipe.
     * @param id : Integer : Identifiant de l'équipe.
     * @return double : Moyenne de l'équipe.
     */
    @GetMapping("/moyenne/{id}")
    public double getMoyenneEquipe(@PathVariable Integer id) {
        return noteAnterieureService.getMoyenneEquipe(id);
    }

    /**
     * @brief Méthode permettant de récupérer la moyenne de l'utilisateur.
     * @param id : Integer : Identifiant de l'utilisateur.
     * @return double : Moyenne de l'utilisateur.
     */
    @GetMapping("/moyenne/utilisateur/{id}")
    public double getMoyenneUtilisateur(@PathVariable Integer id) {
        return noteAnterieureService.getMoyenneUtilisateur(id);
    }

    /**
     * @brief Méthode permettant de modifier le coefficient d'une matière.
     * @param nomMatiere : Matiere : Nom de la matière.
     * @param nouveauCoef : Float : Nouveau coefficient de la matière.
     * @return ResponseEntity<Void> : Réponse HTTP.
     */
    @PutMapping("/coef/modify")
    public ResponseEntity<Void> modifierCoefMatiere(@RequestParam Matiere nomMatiere, @RequestParam Float nouveauCoef) {
        noteAnterieureService.modifierCoefMatiere(nomMatiere, nouveauCoef);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @brief Méthode permettant de récupérer la liste des matières.
     * @return List<Matiere> : Liste des matières.
     */
    @GetMapping("/matieres")
    public List<Matiere> getAllMatieres() {
        return Arrays.asList(Matiere.values());
    }

    /**
     * @brief Méthode permettant de récupérer le coefficient d'une matière.
     * @param matiere : Matiere : Nom de la matière.
     * @return ResponseEntity<Integer> : Réponse HTTP contenant le coefficient de la matière.
     */
    @GetMapping("/{matiere}/coef")
    public ResponseEntity<Integer> getCoefMatiere(@PathVariable Matiere matiere) {
        try {
            Integer coef = noteAnterieureService.getCoefMatiere(matiere);
                return new ResponseEntity<>(coef, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
