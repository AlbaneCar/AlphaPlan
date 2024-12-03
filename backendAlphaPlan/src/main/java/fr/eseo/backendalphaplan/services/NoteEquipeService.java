package fr.eseo.backendalphaplan.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.eseo.backendalphaplan.model.enums.TypeNoteEquipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eseo.backendalphaplan.model.NoteEquipe;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;
import fr.eseo.backendalphaplan.repository.NoteEquipeRepository;

/**
 * @file NoteEquipeService.java
 * @brief Classe service pour les notes d'équipe
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
public class NoteEquipeService {

    // Attributs
    private final NoteEquipeRepository noteEquipeRepository;

    // Constructeur
    @Autowired
    public NoteEquipeService(NoteEquipeRepository noteEquipeRepository) {
        this.noteEquipeRepository = noteEquipeRepository;
    }

    /**
     * @brief Récupérer les notes d'une équipe pour un sprint donné
     * @param id : id de l'équipe
     * @param sprint : numéro du sprint
     * @return Liste des notes
     */
    public List<NoteEquipe> getNotesSprint(Integer id, int sprint) {
        return noteEquipeRepository.getNotesSprint(id, sprint);
    }

    /**
     * @brief Récupérer une note d'une équipe pour un sprint donné
     * @param id : id de l'équipe
     * @param sprint : numéro du sprint
     * @param type : type de la note
     * @return NoteEquipe
     */
    public NoteEquipe getNoteType(Integer id, int sprint, TypeEchelle type) {
        return noteEquipeRepository.getNoteType(id, sprint, type);
    }

    /**
     * @brief Récupérer la moyenne des notes d'une équipe pour un sprint donné
     * @param id : id de l'équipe
     * @param sprint : numéro du sprint
     * @return Moyenne des notes
     */
    public double getMoyenneSprint(Integer id, int sprint) {
        return noteEquipeRepository.getMoyenneSprint(id, sprint);
    }

    public Optional<Double> getMoyenneSprintOptional(Integer equipeId, Integer sprintId) {
        return noteEquipeRepository.getMoyenneSprintOptional(equipeId, sprintId);
    }

    /**
     * @brief Méthode pour sauvegarder une note d'équipe
     * @param noteEquipe : note d'équipe à sauvegarder
     * @return NoteEquipe sauvegardée
     */
    public NoteEquipe saveNoteEquipe(NoteEquipe noteEquipe) {
        // On sauvegarde la note d'équipe
        NoteEquipe savedNote = noteEquipeRepository.save(noteEquipe);
        // On calcule la note 'TE_WO' de l'équipe
        calculTeWo(savedNote);
        // Dans tous les cas, on retourne la note sauvegardée
        return savedNote;
    }

    /**
     * @brief Méthode pour sauvegarder plusieurs notes d'équipe
     * @param noteEquipes : liste des notes d'équipe à sauvegarder
     * @return Liste des notes d'équipe sauvegardées
     */
    public List<NoteEquipe> saveMultipleNoteEquipe(List<NoteEquipe> noteEquipes) {
        List<NoteEquipe> savedNotes = new ArrayList<>();
        for (NoteEquipe noteEquipe : noteEquipes) {
            savedNotes.add(noteEquipeRepository.save(noteEquipe));
            calculTeWo(noteEquipe);
        }
        return savedNotes;
    }

    /**
     * @brief Méthode pour récupérer une note d'équipe via des critères
     * @param noteEquipe : note d'équipe à rechercher
     * @return NoteEquipe
     */
    public Optional<NoteEquipe> findNoteEquipeByCriteria(NoteEquipe noteEquipe) {
        return noteEquipeRepository.findNoteEquipeByEquipeIdAndEvaluateurIdAndSprintId(
                noteEquipe.getEquipe().getId(),
                noteEquipe.getEvaluateur().getId(),
                noteEquipe.getSprint().getId(),
                noteEquipe.getTypeNoteEquipe()
        );
    }

    public List<NoteEquipe> getNotesByEquipeId(Integer idEquipe, Integer idSprint, Integer idEvaluateur) {
            /**
         * @brief Méthode pour récupérer une note d'équipe via l'id de l'équipe et l'id du sprint
         * @param idEquipe : id de l'équipe
         * @param idSprint : id du sprint
         * @param idEvaluateur : id de l'évaluateur
         * @return Liste des notes d'équipe
         */
        return noteEquipeRepository.findNoteEquipeByEquipeIdAndSprintId(
                idEquipe,
                idSprint,
                idEvaluateur
        );
    }

    /**
     * @brief Méthode pour récupérer une note d'équipe via l'id de l'équipe, l'id du sprint et le type de note
     * @param type : type de la note
     * @param sprintId : id du sprint
     * @param equipeId : id de l'équipe
     * @return Liste des notes d'équipe
     */
    public List<NoteEquipe> getNotesByTypeAndTeam(String type, Integer sprintId, Integer equipeId) {
        TypeNoteEquipe typeNoteEquipe = findType(type.toUpperCase());
        return noteEquipeRepository.findNotesByEquipeAndSprintAndType(typeNoteEquipe, sprintId, equipeId);
    }

    /**
     * @brief Méthode pour retrouver le type de note d'équipe via sa chaîne de caractères
     * @param type : type de la note
     * @return Liste des notes d'équipe
     */
    TypeNoteEquipe findType(String type) {
        return switch (type) {
            case "PRMA" -> TypeNoteEquipe.PR_MA;
            case "TESO" -> TypeNoteEquipe.TE_SO;
            case "SPCO" -> TypeNoteEquipe.SP_CO;
            case "SUPR" -> TypeNoteEquipe.SU_PR;
            case "TEWO" -> TypeNoteEquipe.TE_WO;
            case "OTPR" -> TypeNoteEquipe.OT_PR;
            default -> null;
        };
    }

    /**
     * @brief Méthode pour calculer la note 'TE_WO' d'une équipe
     * @param noteEquipe : note d'équipe
     */
    void calculTeWo(NoteEquipe noteEquipe) {
        // On récupère l'équipe concernée par la note ainsi que le sprint
        Integer equipeId = noteEquipe.getEquipe().getId();
        Integer sprintId = noteEquipe.getSprint().getId();
        // On récupère la note moyenne 'PR_MA' de l'équipe pour le sprint
        Double prMa = noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.PR_MA, equipeId, sprintId);
        // On récupère la note moyenne 'TE_SO' de l'équipe pour le sprint
        Double teSo = noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.TE_SO, equipeId, sprintId);
        // On récupère la note moyenne 'SP_CO' de l'équipe pour le sprint
        Double spCo = noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.SP_CO, equipeId, sprintId);
        // On récupère la note moyenne 'SU_PR' de l'équipe pour le sprint
        Double suPr = noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.SU_PR, equipeId, sprintId);
        // Si toutes les notes sont non nulles
        if (prMa != null && teSo != null && spCo != null && suPr != null) {
            // On regarde si une note 'TE_WO' existe déjà pour l'équipe et le sprint
            List<NoteEquipe> temp = noteEquipeRepository.findNotesByEquipeAndSprintAndType(TypeNoteEquipe.TE_WO, equipeId, sprintId);
            // On supprime la note 'TE_WO' si elle existe déjà
            if (!temp.isEmpty()) {
                noteEquipeRepository.deleteAll(temp);
            }
            // On crée une nouvelle note 'TE_WO'
            NoteEquipe teWo = new NoteEquipe();
            teWo.setEquipe(noteEquipe.getEquipe());
            teWo.setSprint(noteEquipe.getSprint());
            teWo.setEvaluateur(noteEquipe.getEvaluateur());
            teWo.setTypeNoteEquipe(TypeNoteEquipe.TE_WO);
            teWo.setNote((prMa + teSo + spCo + suPr) / 4);
            // On sauvegarde dans la BDD
            noteEquipeRepository.save(teWo);
        }
    }


}
