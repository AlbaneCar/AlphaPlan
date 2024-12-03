package fr.eseo.backendalphaplan.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.eseo.backendalphaplan.model.Utilisateur;

import fr.eseo.backendalphaplan.services.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import fr.eseo.backendalphaplan.model.NoteEleve;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import fr.eseo.backendalphaplan.services.NoteEleveService;

/**
 * @file NoteEleveController.java
 * @brief Définition de la classe NoteEleveController.
 * @version 1.0
 * @autor Hugo BOURDAIS
 * @date 01/04/2024
 */
@RestController
@RequestMapping("/api/v1.0/notesEleve")
public class NoteEleveController {

    // Attributs
    private final NoteEleveService noteEleveService;
    private final UtilisateurController utilisateurController;
    private final UtilisateurService utilisateurService;

    // Constructeur
    @Autowired
    public NoteEleveController(NoteEleveService noteEleveService, UtilisateurController utilisateurController, UtilisateurService utilisateurService) {
        this.noteEleveService = noteEleveService;
        this.utilisateurController = utilisateurController;
        this.utilisateurService = utilisateurService;
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
     * @param id : id de l'élève / RequestParam sprint : sprint voulu
     * @return Liste des notes de l'élève
     * @autor Yann LIDEC
     * @date 22/04/2024
     */
    @GetMapping("/{id}/listeNotes")
    public List<NoteEleve> getNotesSprint(@PathVariable Integer id, @RequestParam int sprint) {
        return noteEleveService.getNotesSprint(id, sprint);
    }
    
    /**
     * @brief Méthode permettant de récupérer une note d'élève selon son type sur un sprint
     * @param id : id de l'élève / type : note recherchée / RequestParam sprint : sprint voulu
     * @return Note de l'élève correspondante
     * @autor Yann LIDEC
     * @date 03/05/2024
     */
    @GetMapping("/{id}/{type}")
    public NoteEleve getNoteByType(@PathVariable Integer id, @PathVariable TypeNoteEleve type, @RequestParam int sprint) {
        return noteEleveService.getNoteByType(id, type, sprint);
    }
    
    /**
    * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
    * @param id : id de l'élève / sprint : sprint voulu
    * @return Moyenne de l'élève sur un sprint
    * @autor Yann LIDEC
    * @date 22/04/2024
    */
    @GetMapping("/{id}/moyenne")
    public double getMoyenneSprint(@PathVariable Integer id, @RequestParam int sprint) {
       return  noteEleveService.getMoyenneSprint(id, sprint);
    }

    /**
     * @brief Méthode permettant de modifier une note d'élève
     * @param id : id de la note à modifier
     * @param requestBody : Map<String, Object> : Requête contenant les informations à modifier
     * @return ResponseEntity<NoteEleve> : Réponse HTTP contenant la note modifiée
     */
    @PatchMapping("/{id}")
    public ResponseEntity<NoteEleve> modifierNoteEleve(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody){
        Optional<NoteEleve> note = noteEleveService.getNoteEleve(id);
        if (note.isPresent()) {
            NoteEleve noteEleve = note.get();

            //Modifier la note
            if (requestBody.containsKey("note")) {
                Number noteNumber = (Number) requestBody.get("note");
                noteEleve.setNote((double) noteNumber.floatValue());
            }

            //Modifier le commentaire
            if (requestBody.containsKey("commentaire")) {
                noteEleve.setCommentaire((String) requestBody.get("commentaire"));
            }
            noteEleveService.saveNoteEleve(noteEleve);
            return ResponseEntity.ok(noteEleve);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
    * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint, après les bonus/malus
    * @return Liste des notes de l'élève, avant et après modification
    * @autor Yann LIDEC
    * @date 23/04/2024
    */
    @GetMapping("/{id}/listeNotes/bonusMalus")
    public List<Map<String, Object>> getNotesSprintAfterBM(@PathVariable Integer id, @RequestParam int sprint) {
        return noteEleveService.getNotesSprintAfterBM(id, sprint);
    }
  
    /**
     * @brief Méthode permettant de récupérer la moyenne d'un élève sur un sprint après attribution des bonus/malus
     * @return Moyenne de l'élève sur un sprint après bonus/malus
     * @autor Yann LIDEC
     * @date 23/04/2024
     */
    @GetMapping("/{id}/moyenne/bonusMalus")
    public double getMoyenneSprintAfterBM(@PathVariable Integer id, @RequestParam int sprint) {
        return noteEleveService.getMoyenneSprintAfterBM(id, sprint);
    }

    /**
     * @brief Méthode permettant de récupérer les bonus/malus d'un élève sur un sprint
     * @return Liste des bonus/malus de l'élève
     * @autor Yann LIDEC
     * @date 23/04/2024
     */
    @GetMapping("/{id}/listeBm/{idSprint}")
    public List<NoteEleve> getBonusMalus(@PathVariable Integer id, @PathVariable Integer idSprint) {
        return noteEleveService.getBonusMalusByUserAndSprint(id, idSprint);
    }

    /**
     * @brief Méthode permettant de récupérer les bonus/malus d'une équipe sur un sprint
     * @return Liste des bonus/malus de l'équipe
     * @autor Yann LIDEC
     * @date 23/04/2024
     */
    @GetMapping("/listeBmEquipe/{idEquipe}/{idSprint}")
    public ResponseEntity<List<NoteEleve>> getBonusMalusByEquipe(@PathVariable Integer idEquipe, @PathVariable Integer idSprint) {
        Iterable<Utilisateur> eleves = utilisateurController.getUsersByEquipeId(idEquipe);
        List<NoteEleve> notes = new ArrayList<>();
        for (Utilisateur eleve : eleves) {
            notes.addAll(noteEleveService.getBonusMalusByUserAndSprint(eleve.getId(), idSprint));
        }
        return ResponseEntity.ok(notes);
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'une équipe sur un sprint
     * @return Liste des notes de l'équipe
     * @autor Yann LIDEC
     * @date 23/04/2024
     */
    @GetMapping("/{type}/{sprint}/{nom}/{prenom}")
    public ResponseEntity<List<NoteEleve>> getNotesByTypeAndName(@PathVariable String type, @PathVariable Integer sprint, @PathVariable String nom, @PathVariable String prenom) {
        try {
            Utilisateur utilisateur = utilisateurService.findByNomAndPrenom(nom, prenom);
            List<NoteEleve> notes = noteEleveService.getNotesByTypeAndUser(type, sprint, utilisateur.getId());
            return ResponseEntity.ok(notes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève sur un sprint
     * @return Liste des notes de l'élève
     * @autor Yann LIDEC
     * @date 23/04/2024
     */
    @GetMapping("/{type}/{sprint}/{eleveId}")
    public ResponseEntity<List<NoteEleve>> getNotesByTypeAndName(@PathVariable String type, @PathVariable Integer sprint, @PathVariable Integer eleveId) {
        try {
            List<NoteEleve> notes = noteEleveService.getNotesByTypeAndUser(type, sprint, eleveId);
            return ResponseEntity.ok(notes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @brief Méthode permettant de supprimer une note d'élève
     * @param id : id de la note à supprimer
     * @return
     * @autor Enzo HERBRETEAU
     * @date 03/06/2024
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteEleve(@PathVariable Integer id) {
        noteEleveService.deleteNoteEleve(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAuthority('supervisingStaff:read')")
    @GetMapping("/byEleve/{idEleve}/{idSprint}/{idEvaluateur}")
    public ResponseEntity<List<NoteEleve>> getNotesEleve(@PathVariable Integer idEleve, @PathVariable Integer idSprint, @PathVariable Integer idEvaluateur) {
        List<NoteEleve> notes = noteEleveService.getNotesByEleveId(idEleve, idSprint, idEvaluateur);
        return ResponseEntity.ok(notes);
    }


    @PreAuthorize("hasAnyAuthority('supervisingStaff:write', 'teamMember:write')")
    @PostMapping("/evaluer")
    public ResponseEntity<List<NoteEleve>> noterEleve(@RequestBody List<NoteEleve> noteEleve) {
        List<NoteEleve> notes = new ArrayList<>();
        for (NoteEleve note : noteEleve) {
            // Convertir la chaîne de caractères en énumération TypeNoteEquipe
            String typeNoteEleveString = note.getTypeNoteEleve().toString();
            TypeNoteEleve typeNoteEleve = NoteEleve.convertStringToEnum(typeNoteEleveString);
            note.setTypeNoteEleve(typeNoteEleve);

            // Vérifier si la note pour cet élève par cet utilisateur pour ce sprint existe déjà dans la base de données
            Optional<NoteEleve> existingNote = noteEleveService.findNoteEleveByCriteria(note);
            if (existingNote.isPresent()) {
                // Si la note existe déjà, mettre à jour ses valeurs
                NoteEleve updatedNote = existingNote.get();
                updatedNote.setCommentaire(note.getCommentaire());
                updatedNote.setNote(note.getNote());

                // Mettre à jour la note dans la base de données
                noteEleveService.saveNoteEleve(updatedNote);
                notes.add(updatedNote);
            } else {
                // Si la note n'existe pas, l'ajouter comme une nouvelle note
                NoteEleve newNote = noteEleveService.saveNoteEleve(note);
                notes.add(newNote);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(notes);
    }
}
