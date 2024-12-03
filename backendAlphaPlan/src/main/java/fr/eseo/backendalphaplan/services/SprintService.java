package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEquipe;
import fr.eseo.backendalphaplan.repository.*;

import fr.eseo.backendalphaplan.utils.Note;
import fr.eseo.backendalphaplan.utils.NotesUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @file SprintService.java
 * @brief Définition de la classe SprintService.
 * @version 1.0
 * @date 03/05/2024
 * @autor Hugo BOURDAIS
 */
@Service
public class SprintService {

    // Attributs
    private final SprintRepository sprintRepository;
    private final NoteEleveRepository noteEleveRepository;
    private final NoteEquipeRepository noteEquipeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EquipeRepository equipeRepository;

    // Constructeur
    @Autowired
    public SprintService(SprintRepository sprintRepository, NoteEleveRepository noteEleveRepository, NoteEquipeRepository noteEquipeRepository, UtilisateurRepository utilisateurRepository, EquipeRepository equipeRepository) {
        this.sprintRepository = sprintRepository;
        this.noteEleveRepository = noteEleveRepository;
        this.noteEquipeRepository = noteEquipeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.equipeRepository = equipeRepository;
    }

    /**
     * @return la liste des sprints
     * @details Récupère tous les sprints
     * @date 03/05/2024
     * @autor Hugo BOURDAIS
     */
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

    /**
     * @param id : l'identifiant du sprint
     * @return le sprint
     * @details Récupère un sprint à partir de son identifiant
     * @date 03/05/2024
     * @autor Hugo BOURDAIS
     */
    public Optional<Sprint> getSprintById(Integer id) {
        return sprintRepository.findById(id);
    }

    /**
     * @param name : le nom du sprint
     * @return le sprint
     * @details Récupère un sprint à partir de son nom
     * @date 03/05/2024
     * @autor Hugo BOURDAIS
     */
    public Sprint getSprintByName(String name) {
        return sprintRepository.findByName(name);
    }

    /**
     * @param sprints : la liste des sprints à sauvegarder
     * @return la liste des sprints sauvegardés
     * @details Sauvegarde une liste de sprints
     * @date 03/05/2024
     * @autor Hugo BOURDAIS
     */
    public List<Sprint> saveSprints(List<Sprint> sprints) {
        List<Sprint> savedSprints = new ArrayList<>();
        for (Sprint sprint : sprints) {
            savedSprints.add(sprintRepository.save(sprint));
        }
        return savedSprints;
    }

    /**
     * @param id : le sprint à supprimer
     * @details Supprime un sprint à partir de son identifiant
     * @date 03/05/2024
     * @author Hugo BOURDAIS
     */
    public void deleteSprint(Integer id) {
        sprintRepository.deleteById(id);
    }

    /**
     * @param id : l'identifiant de l'élève
     * @param sprint : l'identifiant du sprint
     * @return la liste des notes
     * @details Récupère les notes d'élève d'un sprint à partir de son identifiant
     * @author Hugo BOURDAIS
     * @date 03/05/2024
     */
    public List<Note> getNoteEleveByEleveAndSprint(Integer id, Integer sprint) {
        // On récupère l'équipe de l'élève
        Optional<Utilisateur> eleve = utilisateurRepository.findById(id);
        // Si l'élève n'existe pas, on lance une exception
        if (eleve.isEmpty()) {
            throw new IllegalArgumentException("L'élève n'existe pas");
        }
        // Si le sprint n'existe pas, on lance une exception
        Optional<Sprint> sprintOptional = sprintRepository.findById(sprint);
        if (sprintOptional.isEmpty()) {
            throw new IllegalArgumentException("Le sprint n'existe pas");
        }

        // Sinon on récupère l'équipe de l'élève
        Integer equipe = eleve.get().getEquipe().getId();

        // On récupère les valeurs de l'énumération TypeNoteEleve
        List<TypeNoteEleve> typesNoteEleve = List.of(TypeNoteEleve.values());

        // On récupère les valeurs de l'énumération TypeNoteEquipe
        List<TypeNoteEquipe> typesNoteEquipe = List.of(TypeNoteEquipe.values());

        // On crée une map pour stocker les notes de chaque type
        List<Note> notes = new ArrayList<>();

        // Pour chaque type de note, on récupère la note de l'élève pour le sprint donné
        for (TypeNoteEleve type : typesNoteEleve) {
            // On ajoute la note à la map des notes si elle n'est pas nulle sinon on ajoute "ND"
            Double note = noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(type, id, sprint);
            // Si la note à plus que 2 chiffres après la virgule, on la tronque
            if (note != null) {
                note = Math.round(note * 100.0) / 100.0;
            }
            addToMap(notes, note, type.name());
        }
        for (TypeNoteEquipe type : typesNoteEquipe) {
            // On ajoute la note à la map des notes si elle n'est pas nulle sinon on ajoute "ND"
            Double note = noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(type, equipe, sprint);
            // Si la note à plus que 2 chiffres après la virgule, on la tronque
            if (note != null) {
                note = Math.round(note * 100.0) / 100.0;
            }
            addToMap(notes, note, type.name());
        }

        // On retourne la map des notes
        return notes;
    }

    /**
     * @param notes : la liste des notes
     * @param note : la note à ajouter
     * @param tag : le tag de la note
     * @details Ajoute une note à la liste des notes
     * @date 03/05/2024
     * @author Hugo BOURDAIS
     */
    private void addToMap(List<Note> notes, Double note, String tag) {
        // On crée les attributs de la note
        String type = decodeTag(tag);
        String noteString = Objects.requireNonNullElse(note, "-").toString();
        String coeff = defCoeffByTag(tag);
        // On instancie la note
        Note newNote = new Note(tag, type, noteString, coeff);
        // On ajoute la note à la liste
        notes.add(newNote);
    }

    /**
     * @param tag : le tag de la note
     * @return le type de la note
     * @details Décode le tag de la note
     * @date 03/05/2024
     * @author Hugo BOURDAIS
     */
    private String decodeTag(String tag) {
        return switch (tag) {
            case "TE_WO" -> "Team Work";
            case "PR_MA" -> "Project Management";
            case "TE_SO" -> "Technical Solution";
            case "SP_CO" -> "Sprint Conformity";
            case "SU_PR" -> "Support Presentation";
            case "IN_SP" -> "Individual Sprint";
            case "TE_BM" -> "Team Bonus";
            case "SS_BM" -> "Supervising Staff Bonus";
            case "IN_PR" -> "Individual Presentation";
            case "SS_PR" -> "Supervising Staff Presentation";
            case "OT_PR" -> "Other Presentation";
            case "TC_PR" -> "Technical Coaches Presentation";
            case "IG_SP" -> "Individual Global Sprint";
            default -> "ND";
        };
    }

    /**
     * @param tag : le tag de la note
     * @return le coefficient de la note
     * @details Définit le coefficient de la note
     * @date 30/05/2024
     * @autor Hugo BOURDAIS
     */
    private String defCoeffByTag(String tag) {
        return switch (tag) {
            case "SS_BM", "TE_BM", "TE_WO" -> "-";
            case "SS_PR" -> "2";
            case "IN_SP" -> "0,7";
            case "IN_PR" -> "0,3";
            default -> "1";
        };
    }

    /**
     * @details Supprime tous les sprints
     * @date 03/05/2024
     * @author Albane CARTERON
     */
    public void deleteAllSprints() {
        sprintRepository.deleteAll();
    }

    /**
     * @param equipeId : l'identifiant de l'équipe
     * @param sprintId : l'identifiant du sprint
     * @return la liste des notes
     * @details Récupère les notes d'équipe d'un sprint à partir de son identifiant
     * @date 30/05/2024
     * @autor Hugo BOURDAIS
     */
    public List<NotesUtilisateur> getNoteEquipeByEquipeAndSprint(Integer equipeId, Integer sprintId) {
        // Si l'équipe n'existe pas, on lance une exception
        Optional<Equipe> equipe = equipeRepository.findById(equipeId);
        if (equipe.isEmpty()) {
            throw new IllegalArgumentException("L'équipe n'existe pas");
        }

        // Si le sprint n'existe pas, on lance une exception
        Optional<Sprint> sprintOptional = sprintRepository.findById(sprintId);
        if (sprintOptional.isEmpty()) {
            throw new IllegalArgumentException("Le sprint n'existe pas");
        }

        // Sinon on récupère la liste des élèves de l'équipe
        List<Utilisateur> eleves = equipeRepository.getStudentsByEquipeId(equipeId);

        // On crée une map pour stocker les notes de chaque type
        List<NotesUtilisateur> notes = new ArrayList<>();

        // pour chaque élève de l'équipe, on récupère les notes de chaque type
        for (Utilisateur eleve : eleves) {
            // On récupère le nom et le prénom de l'élève
            String nom = eleve.getNom();
            String prenom = eleve.getPrenom();

            // Pour chaque type de note, on récupère la note de l'équipe pour le sprint donné
            String prMa = Objects.requireNonNullElse(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.PR_MA, equipeId, sprintId), "-").toString();
            String teSo = Objects.requireNonNullElse(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.TE_SO, equipeId, sprintId), "-").toString();
            String spCo = Objects.requireNonNullElse(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.SP_CO, equipeId, sprintId), "-").toString();
            String suPr = Objects.requireNonNullElse(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.SU_PR, equipeId, sprintId), "-").toString();
            String teWo = Objects.requireNonNullElse(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.TE_WO, equipeId, sprintId), "-").toString();
            String otPr = Objects.requireNonNullElse(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.OT_PR, equipeId, sprintId), "-").toString();

            String ssBm = Objects.requireNonNullElse(noteEleveRepository.findSSBMByUtilisateurAndSprint(eleve.getId(), sprintId), "-").toString();
            String teBm = Objects.requireNonNullElse(noteEleveRepository.findTEBMByUtilisateurAndSprint(eleve.getId(), sprintId), "-").toString();

            String inSp = Objects.requireNonNullElse(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IN_SP, eleve.getId(), sprintId), "-").toString();
            String ssPr = Objects.requireNonNullElse(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.SS_PR, eleve.getId(), sprintId), "-").toString();
            String inPr = Objects.requireNonNullElse(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IN_PR, eleve.getId(), sprintId), "-").toString();
            String igSp = Objects.requireNonNullElse(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IG_SP, eleve.getId(), sprintId), "-").toString();

            // On ajoute les notes à la map des notes
            NotesUtilisateur newNote = new NotesUtilisateur(nom, prenom, prMa, teSo, spCo, suPr, teWo, teBm, ssBm, inSp, ssPr, otPr, inPr, igSp);
            notes.add(newNote);
        }
        return notes;
    }

    /**
     * @param sprint : le sprint à mettre à jour
     * @return le sprint mis à jour
     * @details Met à jour un sprint
     * @date 03/05/2024
     * @autor Hugo BOURDAIS
     */
    public Sprint updateSprint(Sprint sprint) {
        return sprintRepository.save(sprint);
    }
}
