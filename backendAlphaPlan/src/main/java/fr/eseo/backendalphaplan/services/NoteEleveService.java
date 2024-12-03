package fr.eseo.backendalphaplan.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEquipe;
import fr.eseo.backendalphaplan.repository.NoteEquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eseo.backendalphaplan.model.NoteEleve;
import fr.eseo.backendalphaplan.repository.NoteEleveRepository;

/**
 * @file NoteEleveService.java
 * @brief Classe service pour les notes des élèves
 * @version 1.0
 * @date 01/04/2024
 */
@Service
public class NoteEleveService {

    // Attributs
    private final NoteEleveRepository noteEleveRepository;
    private final NoteEquipeRepository noteEquipeRepository;

    // Constructeur
    @Autowired
    public NoteEleveService(NoteEleveRepository noteEleveRepository, NoteEquipeRepository noteEquipeRepository) {
        this.noteEleveRepository = noteEleveRepository;
        this.noteEquipeRepository = noteEquipeRepository;
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
     * @param id : id de l'élève
     * @param sprint : numéro du sprint
     * @return Liste des notes de l'élève
     */
    public List<NoteEleve> getNotesSprint(Integer id, int sprint) {
        return noteEleveRepository.getNotesSprint(id, sprint);
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
     * @param id : id de l'élève
     * @param type : type de note
     * @param sprint : numéro du sprint
     * @return Note de l'élève
     */
    public NoteEleve getNoteByType(Integer id, TypeNoteEleve type, int sprint) {
        return noteEleveRepository.getNoteByType(id, type, sprint);
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
     * @param id : id de l'élève
     * @param sprint : numéro du sprint
     * @return Liste des notes de l'élève
     */
    public double getMoyenneSprint(Integer id, int sprint) {
        return noteEleveRepository.getMoyenneSprint(id, sprint);
    }

    /**
     * @brief Méthode permettant de sauvegarder une note
     * @param noteEleve : note à sauvegarder
     * @return Note sauvegardée
     */
    public NoteEleve saveNoteEleve(NoteEleve noteEleve) {
        // On sauvegarde la note
        NoteEleve savedNote = noteEleveRepository.save(noteEleve);
        // On calcule la note 'IN_PR' de l'élève
        calculInPr(savedNote);
        // On calcule la note 'IN_SP' de l'élève
        calculInSp(savedNote);
        // On calcule la note 'IG_SP' de l'élève
        calculIgSp(savedNote);
        // Dans tous les cas, on retourne la note sauvegardée
        return savedNote;

    }    

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
     * @param id : id de l'élève
     * @param sprint : numéro du sprint
     * @return Liste des notes de l'élève
     */
    public List<Map<String, Object>> getNotesSprintAfterBM(Integer id, int sprint) {
        return noteEleveRepository.getNotesSprintAfterBM(id, sprint);
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
     * @param id : id de l'élève
     * @param sprint : numéro du sprint
     * @return Liste des notes de l'élève
     */
    public double getMoyenneSprintAfterBM(Integer id, int sprint) {
        return noteEleveRepository.getMoyenneSprintAfterBM(id, sprint);
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
     * @param id : id de l'élève
     * @param sprint : numéro du sprint
     * @return Liste des notes de l'élève
     */
    public List<NoteEleve> getBonusMalusByUserAndSprint(Integer id, int sprint) {
        List<NoteEleve> teBm = noteEleveRepository.findTe_BMbyUtilisateurAndSprint(id, sprint);
        List<NoteEleve> ssBm = noteEleveRepository.findSs_BMbyUtilisateurAndSprint(id, sprint);
        return List.of(teBm, ssBm).stream().flatMap(List::stream).toList();
    }

    /**
     * @brief Méthode permettant de récupérer les notes via leur id
     * @param id : id de la note
     * @return Liste des notes
     */
    public Optional<NoteEleve> getNoteEleve(Integer id){
        return noteEleveRepository.findById(id);
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
     * @param type : type de note
     * @param sprintId : id du sprint
     * @param utilisateurId : id de l'élève
     * @return Liste des notes de l'élève
     * @autor BOURDAIS Hugo
     * @date 22/04/2024
     */
    public List<NoteEleve> getNotesByTypeAndUser(String type, Integer sprintId, Integer utilisateurId) {
        TypeNoteEleve typeNoteEleve = findType(type.toUpperCase());
        return noteEleveRepository.findNoteEleveByTypeAndUser(typeNoteEleve, sprintId, utilisateurId);
    }

    public void deleteNoteEleve(Integer id) {
        noteEleveRepository.deleteById(id);
    }

    /**
     * @brief Méthode permettant de récupérer le type d'une note via sa chaine de caractère
     * @param type : type de note
     * @return TypeNoteEleve
     * @autor BOURDAIS Hugo
     * @date 22/04/2024
     */
    private TypeNoteEleve findType(String type) {
        return switch (type) {
            case "SSPR" -> TypeNoteEleve.SS_PR;
            case "INPR" -> TypeNoteEleve.IN_PR;
            case "TEBM" -> TypeNoteEleve.TE_BM;
            case "SSBM" -> TypeNoteEleve.SS_BM;
            case "INSP" -> TypeNoteEleve.IN_SP;
            case "IGSP" -> TypeNoteEleve.IG_SP;
            default -> null;
        };
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
     * @param idEleve : id de l'élève
     * @param idSprint : id du sprint
     * @param idEvaluateur : id de l'évaluateur
     * @return Liste des notes de l'élève
     * @autor BOURDAIS Hugo
     * @date 22/04/2024
     */
    public List<NoteEleve> getNotesByEleveId (Integer idEleve,Integer idSprint, Integer idEvaluateur){
        return noteEleveRepository.getNotesEleves(idEleve, idSprint, idEvaluateur);
    }

    /**
     * @brief Méthode permettant de récupérer les notes d'un élève d'un sprint
     * @param noteEleve : note d'équipe à rechercher
     * @return NoteEleve
     * @autor BOURDAIS Hugo
     * @date 22/04/2024
     */
    public Optional<NoteEleve> findNoteEleveByCriteria(NoteEleve noteEleve) {
        return noteEleveRepository.findNoteEleveByEleveIdAndEvaluateurIdAndSprintId(
                noteEleve.getEleve().getId(),
                noteEleve.getEvaluateur().getId(),
                noteEleve.getSprint().getId(),
                noteEleve.getTypeNoteEleve()
        );
    }

    /**
     * Méthode permettant de calculer la note 'IG_SP' d'un élève
     * @param noteEleve la note de l'élève
     */
    public void calculIgSp(NoteEleve noteEleve) {
        // On récupère l'élève concerné par la note ainsi que le sprint
        Sprint sprint = noteEleve.getSprint();
        Utilisateur eleve = noteEleve.getEleve();
        // Si l'élève et le sprint sont non nuls
        if (sprint != null && eleve != null) {
            // On récupère la note moyenne 'IN_SP' de l'élève pour le sprint
            Double inSp = this.noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IN_SP, eleve.getId(), sprint.getId());
            // On récupère la note moyenne 'IN_PR' de l'élève pour le sprint
            Double inPr = this.noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IN_PR, eleve.getId(), sprint.getId());
            // Si les notes sont non nulles
            if (inSp != null && inPr != null) {
                // On regarde si une note 'IG_SP' existe déjà pour l'élève et le sprint
                List<NoteEleve> temp = this.noteEleveRepository.findNoteEleveByTypeAndUser(TypeNoteEleve.IG_SP, sprint.getId(), eleve.getId());
                // On supprime la note 'IG_SP' si elle existe déjà
                if (temp != null) {
                    this.noteEleveRepository.deleteAll(temp);
                }
                // On crée une nouvelle note 'IG_SP' pour l'élève et le sprint
                NoteEleve igSp = new NoteEleve();
                igSp.setEleve(eleve);
                igSp.setSprint(sprint);
                igSp.setTypeNoteEleve(TypeNoteEleve.IG_SP);
                igSp.setNote(0.7 * inSp + 0.3 * inPr);
                // On sauvegarde la note 'IG_SP'
                this.noteEleveRepository.save(igSp);
            }
        }
    }

    /**
     * Méthode permettant de calculer la note 'IN_PR' d'un élève
     * @param noteEleve la note de l'élève
     */
    public void calculInPr(NoteEleve noteEleve) {
        // On récupère l'élève concerné par la note ainsi que le sprint
        Sprint sprint = noteEleve.getSprint();
        Utilisateur eleve = noteEleve.getEleve();
        // Si l'élève et le sprint sont non nuls
        if (sprint != null && eleve != null) {
            // On récupère la note moyenne 'SS_PR' de l'élève pour le sprint
            Double ssPr = this.noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.SS_PR, eleve.getId(), sprint.getId());
            // On récupère la note moyenne 'OT_PR' de l'élève pour le sprint
            Double otPr = this.noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.OT_PR, eleve.getId(), sprint.getId());
            // Si les notes sont non nulles
            if (ssPr != null && otPr != null) {
                // On regarde si une note 'IN_PR' existe déjà pour l'élève et le sprint
                List<NoteEleve> temp = this.noteEleveRepository.findNoteEleveByTypeAndUser(TypeNoteEleve.IN_PR, sprint.getId(), eleve.getId());
                // On supprime la note 'IN_PR' si elle existe déjà
                if (temp != null) {
                    this.noteEleveRepository.deleteAll(temp);
                }
                // On crée une nouvelle note 'IN_PR' pour l'élève et le sprint
                NoteEleve inPr = new NoteEleve();
                inPr.setEleve(eleve);
                inPr.setSprint(sprint);
                inPr.setTypeNoteEleve(TypeNoteEleve.IN_PR);
                inPr.setNote((2 * ssPr + 1 * otPr) / 3);
                // On sauvegarde la note 'IN_PR'
                this.noteEleveRepository.save(inPr);
            }
        }
    }

    /**
     * Méthode permettant de calculer la note 'IN_SP' d'un élève
     * @param noteEleve la note de l'élève
     */
    public void calculInSp(NoteEleve noteEleve) {
        // On récupère l'élève concerné par la note ainsi que le sprint
        Sprint sprint = noteEleve.getSprint();
        Utilisateur eleve = noteEleve.getEleve();
        // Si l'élève et le sprint sont non nuls
        if (sprint != null && eleve != null) {
            // On récupère la note moyenne 'TE_WO' de l'élève pour le sprint
            Double teWo = this.noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.TE_WO, eleve.getId(), sprint.getId());
            // On récupère la somme des 'TE_BM' de l'élève pour le sprint
            Double teBm = this.noteEleveRepository.findTEBMByUtilisateurAndSprint(eleve.getId(), sprint.getId());
            // On récupère la somme des 'SS_BM' de l'élève pour le sprint
            Double ssBm = this.noteEleveRepository.findSSBMByUtilisateurAndSprint(eleve.getId(), sprint.getId());
            // Si les notes sont non nulles
            if (teWo != null && teBm != null && ssBm != null) {
                // On regarde si une note 'IN_SP' existe déjà pour l'élève et le sprint
                List<NoteEleve> temp = this.noteEleveRepository.findNoteEleveByTypeAndUser(TypeNoteEleve.IN_SP, sprint.getId(), eleve.getId());
                // On supprime la note 'IN_SP' si elle existe déjà
                if (temp != null) {
                    this.noteEleveRepository.deleteAll(temp);
                }
                // On crée une nouvelle note 'IN_SP' pour l'élève et le sprint
                NoteEleve inSp = new NoteEleve();
                inSp.setEleve(eleve);
                inSp.setSprint(sprint);
                inSp.setTypeNoteEleve(TypeNoteEleve.IN_SP);
                inSp.setNote(1 * teWo + 1 * teBm + 1 * ssBm);
                // On sauvegarde la note 'IN_SP'
                this.noteEleveRepository.save(inSp);
            }
        }
    }
}
