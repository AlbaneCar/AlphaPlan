package fr.eseo.backendalphaplan.controller;


import fr.eseo.backendalphaplan.model.EchelleNote;
import fr.eseo.backendalphaplan.model.Echelon;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;
import fr.eseo.backendalphaplan.services.EchelleNoteService;
import fr.eseo.backendalphaplan.services.EchelonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @file EchelleNoteController.java
 * @brief La classe EchelleNoteController est le controller qui permet de gérer les requêtes HTTP concernant les échelles de notes
 * @version 1.0
 * @date 20/05/2024
 * @autor Hugo ROULLIER
 */
@RestController
@RequestMapping("/api/echelleNotes")
public class EchelleNoteController {

    // Attributs
    private final EchelleNoteService echelleNoteService;
    private EchelonService echelonService;

    // Constructeur
    @Autowired
    public EchelleNoteController(EchelleNoteService echelleNoteService,  EchelonService echelonService) {
        this.echelleNoteService = echelleNoteService;
        this.echelonService = echelonService;
    }

    /**
     * @brief Méthode permettant de récupérer toutes les échelles de notes
     * @return Liste des échelles de notes
     * @autor Hugo ROULLIER
     * @date 20/05/2024
     */
    @GetMapping
    public List<EchelleNote> getAllEchelleNotes() {
        List<EchelleNote> echelleNotes = echelleNoteService.getAllEchelleNotes();
        for (EchelleNote echelleNote : echelleNotes) {
            List<Echelon> echelons = echelonService.getEchelonsByEchelleNoteId(echelleNote);
            echelleNote.setEchelons(echelons);
        }
        return echelleNotes;
    }

    /**
     * @brief Méthode permettant d'ajouter une échelle de notes
     * @param echelleNote : échelle de notes à ajouter
     * @autor Hugo ROULLIER
     * @date 20/05/2024
     */
    @PostMapping("/ajouter")
    public void ajouterEchelleNote(@RequestBody EchelleNote echelleNote) {
        // Add the EchelleNote
        EchelleNote savedEchelleNote = echelleNoteService.ajouterEchelle(echelleNote.getTypeEchelle(), echelleNote.getDescription());

        // Check if echelons is not null before iterating over it
        if (echelleNote.getEchelons() != null) {
            // Create new Echelons and associate them with the EchelleNote
            for (Echelon echelonData : echelleNote.getEchelons()) {
                Echelon echelon = new Echelon();
                echelon.setEchelleNoteId(savedEchelleNote);
                echelon.setCommentaire(echelonData.getCommentaire());
                echelon.setNote1(echelonData.getNote1());
                echelon.setNote2(echelonData.getNote2());
                echelonService.save(echelon);
            }
        }
    }

    /**
     * @brief Méthode permettant de supprimer une échelle de notes
     * @param id : id de l'échelle de notes à supprimer
     * @autor Hugo ROULLIER
     * @date 20/05/2024
     */
    @DeleteMapping("/supprimer/{id}")
    public void supprimerEchelleNote(@PathVariable Integer id) {
        EchelleNote echelleNote = echelleNoteService.getEchelleNoteById(id);
        List<Echelon> echelons = echelonService.getEchelonsByEchelleNoteId(echelleNote);
        for (Echelon echelon : echelons) {
            echelonService.delete(echelon);
        }
        echelleNoteService.supprimerEchelle(id);
    }

    /**
     * @brief Méthode permettant de récupérer les types d'échelles de notes
     * @return Liste des types d'échelles de notes
     * @autor Hugo ROULLIER
     * @date 20/05/2024
     */
    @GetMapping("/types")
    public List<TypeEchelle> getAllTypeEchelles() {
        return Arrays.asList(TypeEchelle.values());
    }

    /**
     * @brief Méthode permettant de récupérer les types d'échelles de notes déjà utilisés
     * @return Liste des types d'échelles de notes déjà utilisés
     * @autor Hugo ROULLIER
     * @date 04/06/2024
     */
    @GetMapping("/useTypes")
    public List<TypeEchelle> getUsedTypesEchelle() {
        return echelleNoteService.getAllEchelleNotes()
                .stream()
                .map(EchelleNote::getTypeEchelle)
                .distinct()
                .collect(Collectors.toList());
    }

}
