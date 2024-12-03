package fr.eseo.backendalphaplan.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.eseo.backendalphaplan.dto.MoyenneEquipeResponse;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEquipe;
import fr.eseo.backendalphaplan.services.EquipeService;
import fr.eseo.backendalphaplan.services.SprintService;
import fr.eseo.backendalphaplan.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.eseo.backendalphaplan.model.NoteEquipe;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;
import fr.eseo.backendalphaplan.services.NoteEquipeService;

/**
 * @file NoteEquipeController.java
 * @brief Définition de la classe NoteEquipeController.
 * @version 1.0
 * @autor Hugo BOURDAIS
 * @date 24/04/2024
 */
@RestController
@RequestMapping("/api/v1.0/notesEquipe")
@RequiredArgsConstructor
public class NoteEquipeController {

    // Attributs
    private final NoteEquipeService noteEquipeService;
    private final UtilisateurService utilisateurService;
    private final EquipeService equipeService;
    private final SprintService sprintService;


    @PreAuthorize("hasAnyAuthority('supervisingStaff:read', 'teamMember:read', 'technicalCoaches:read', 'juryMember:read')")
    @GetMapping("/byEquipe/{idEquipe}/{idSprint}/{idEvaluateur}")
        public ResponseEntity<List<NoteEquipe>> getNotesEquipe(@PathVariable Integer idEquipe, @PathVariable Integer idSprint, @PathVariable Integer idEvaluateur) {
        List<NoteEquipe> notes = noteEquipeService.getNotesByEquipeId(idEquipe, idSprint, idEvaluateur);
        return ResponseEntity.ok(notes);
    }

    /**
     * @param id : id de l'équipe / RequestParam sprint : sprint voulu
     * @return Liste des notes de l'équipe
     * @brief Méthode permettant de récupérer les notes d'une équipe d'un sprint
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @GetMapping("/{id}/listeNotes")
    public List<NoteEquipe> getNotesSprint(@PathVariable Integer id, @RequestParam int sprint) {
        return noteEquipeService.getNotesSprint(id, sprint);
    }

    /**
     * @param id : id de l'équipe / RequestParam sprint : sprint voulu / RequestParam type : type de note voulu
     * @return Note de l'équipe du type renseigné
     * @brief Méthode permettant de récupérer une note d'un type en particulier
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @GetMapping("/{id}/noteType")
    public NoteEquipe getNoteType(@PathVariable Integer id, @RequestParam int sprint, @RequestParam TypeEchelle type) {
        return noteEquipeService.getNoteType(id, sprint, type);
    }

    /**
     * @param id : id de l'équipe / RequestParam sprint : sprint voulu
     * @return Moyenne de l'équipe sur un sprint
     * @brief Méthode permettant de récupérer la moyenne des NoteEquipe d'une équipe sur un sprint
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @GetMapping("/{id}/moyenne")
    public double getMoyenneSprint(@PathVariable Integer id, @RequestParam int sprint) {
        return noteEquipeService.getMoyenneSprint(id, sprint);
    }

    /**
     * @param noteEquipe : NoteEquipe
     * @return Validation de l'insertion de la note
     * @brief Méthode permettant de noter une équipe
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @PostMapping("/evaluer")
    public ResponseEntity<NoteEquipe> noterEquipe(@RequestBody NoteEquipe noteEquipe) {
        NoteEquipe note = noteEquipeService.saveNoteEquipe(noteEquipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    /**
     * @param noteEquipes : Liste de NoteEquipe
     * @return Validation de l'insertion des notes
     * @brief Méthode permettant de noter plusieurs équipes
     */
    @PreAuthorize("hasAnyAuthority('supervisingStaff:read', 'teamMember:read', 'technicalCoaches:read', 'juryMember:read')")
    @PostMapping("/evaluerMultiple")
    public ResponseEntity<List<NoteEquipe>> noterEquipeMultiple(@RequestBody List<NoteEquipe> noteEquipes) {
        List<NoteEquipe> notes = new ArrayList<>();
        for (NoteEquipe noteEquipe : noteEquipes) {
            // Convertir la chaîne de caractères en énumération TypeNoteEquipe
            String typeNoteEquipeString = noteEquipe.getTypeNoteEquipe().toString();
            TypeNoteEquipe typeNoteEquipe = NoteEquipe.convertStringToEnum(typeNoteEquipeString);
            noteEquipe.setTypeNoteEquipe(typeNoteEquipe);
            // Vérifier si la note pour cette équipe par cet utilisateur pour ce sprint existe déjà dans la base de données
            Optional<NoteEquipe> existingNote = noteEquipeService.findNoteEquipeByCriteria(noteEquipe);
            if (existingNote.isPresent()) {
                // Si la note existe déjà, mettre à jour ses valeurs
                NoteEquipe updatedNote = existingNote.get();
                updatedNote.setCommentaire(noteEquipe.getCommentaire());
                updatedNote.setNote(noteEquipe.getNote());

                // Mettre à jour la note dans la base de données
                noteEquipeService.saveNoteEquipe(updatedNote);
                notes.add(updatedNote);
            } else {
                // Si la note n'existe pas, l'ajouter comme une nouvelle note
                NoteEquipe newNote = noteEquipeService.saveNoteEquipe(noteEquipe);
                notes.add(newNote);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(notes);
    }

    /**
     * @param type : Type de note / sprint : Sprint voulu / nom : Nom de l'utilisateur / prenom : Prénom de l'utilisateur
     * @return Liste des notes de l'utilisateur
     * @brief Méthode permettant de récupérer les notes d'un utilisateur
     */
    @GetMapping("/{type}/{sprint}/{nom}/{prenom}")
    public ResponseEntity<List<NoteEquipe>> getNotesByTypeAndName(@PathVariable String type, @PathVariable Integer sprint, @PathVariable String nom, @PathVariable String prenom) {
        try {
            Utilisateur utilisateur = utilisateurService.findByNomAndPrenom(nom, prenom);
            Equipe equipe = utilisateur.getEquipe();
            List<NoteEquipe> notes = noteEquipeService.getNotesByTypeAndTeam(type, sprint, equipe.getId());
            return ResponseEntity.ok(notes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @param type : Type de note / sprint : Sprint voulu / eleve : Id de l'utilisateur
     * @return Liste des notes de l'utilisateur
     * @brief Méthode permettant de récupérer les notes d'un utilisateur
     */
    @GetMapping("/{type}/{sprint}/{eleve}")
    public ResponseEntity<List<NoteEquipe>> getNotesByTypeAndName(@PathVariable String type, @PathVariable Integer sprint, @PathVariable Integer eleve) {
        try {
            Utilisateur utilisateur = utilisateurService.getUtilisateurById(eleve);
            Equipe equipe = utilisateur.getEquipe();
            List<NoteEquipe> notes = noteEquipeService.getNotesByTypeAndTeam(type, sprint, equipe.getId());
            return ResponseEntity.ok(notes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/moyennes")
    public ResponseEntity<List<MoyenneEquipeResponse>> getMoyennesEquipes() {
        List<Sprint> sprints = sprintService.getAllSprints();
        List<Equipe> equipes = equipeService.getAllEquipes();
        List<MoyenneEquipeResponse> moyennes = new ArrayList<>();

        for(Equipe equipe : equipes) {
            for(Sprint sprint : sprints) {
                MoyenneEquipeResponse moyenne = MoyenneEquipeResponse
                        .builder()
                        .idEquipe(equipe.getId())
                        .idSprint(sprint.getId())
                        .moyenne(noteEquipeService.getMoyenneSprintOptional(equipe.getId(), sprint.getId()).orElse(null))
                        .date(sprint.getStartDate())
                        .build();

                moyennes.add(moyenne);
            }
        }

        return ResponseEntity.ok(moyennes);
    }

}
