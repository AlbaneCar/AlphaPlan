package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.NoteEleve;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @file NoteEleveRepository.java
 * @brief Interface repository pour les NoteEleve
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface NoteEleveRepository extends JpaRepository<NoteEleve, Integer> {

    /**
     * @brief Requête pour récupérer les notes d'un étudiant pour un sprint donné
     * @param id : id de l'étudiant
     * @param sprint : numéro du sprint
     * @return Liste des notes
     */
    @Query("SELECT n FROM NoteEleve n WHERE n.eleve.id = :id AND n.sprint.id = :sprint")
    List<NoteEleve> getNotesSprint(Integer id, int sprint);

    /**
     * @brief Requête pour récupérer une note via son id
     * @param id : id de la note
     * @return NoteEleve
     */
    @Query("SELECT n FROM NoteEleve n WHERE n.eleve.id = :id AND n.sprint.id = :sprint AND n.typeNoteEleve = :type")
    NoteEleve getNoteByType(Integer id, TypeNoteEleve type, int sprint);

    /**
     * @brief Requête pour récupérer la moyenne des notes d'un étudiant pour un sprint donné
     * @param id : id de l'étudiant
     * @param sprint : numéro du sprint
     * @return Moyenne des notes
     */
    @Query("SELECT AVG(n.note) FROM NoteEleve n WHERE n.eleve.id = :id AND n.sprint.id = :sprint")
    double getMoyenneSprint(Integer id, int sprint);

    /**
     * @brief Requête pour récupérer les notes d'un étudiant pour un sprint donné avec les bonus/malus
     * @param id : id de l'étudiant
     * @param sprint : numéro du sprint
     * @return Liste des notes avec les bonus/malus
     */
    @Query("SELECT new map(n as noteEleve, (n.note + COALESCE(SUM(bm.valeur), 0)) as noteAjustee, n.note as noteOriginale, bm as bonusMalus) FROM NoteEleve n LEFT JOIN BonusMalus bm ON bm.noteEleve.id = n.id WHERE n.eleve.id = :id AND n.sprint.id = :sprint GROUP BY n")
    List<Map<String, Object>> getNotesSprintAfterBM(Integer id, int sprint);

    /**
     * @brief Requête pour récupérer la moyenne des notes d'un étudiant pour un sprint donné avec les bonus/malus
     * @param id : id de l'étudiant
     * @param sprint : numéro du sprint
     * @return Moyenne des notes avec les bonus/malus
     */
    @Query("SELECT AVG(sub.noteAjustee) FROM (SELECT (n.note + COALESCE(SUM(bm.valeur), 0)) as noteAjustee FROM NoteEleve n LEFT JOIN BonusMalus bm ON bm.noteEleve.id = n.id WHERE n.eleve.id = :id AND n.sprint.id = :sprint GROUP BY n) as sub")
    double getMoyenneSprintAfterBM(Integer id, int sprint);

    /**
     * @brief Requête pour récupérer la moyenne des notes d'un étudiant pour un sprint, en fonction du type de note* @param typeNoteEleve : type de note
     * @param utilisateur : id de l'étudiant
     * @param sprint : numéro du sprint
     * @param typeNoteEleve : type de note
     * @return Moyenne des notes
     */
    @Query("SELECT AVG(n.note) FROM NoteEleve n WHERE n.typeNoteEleve = :typeNoteEleve AND n.eleve.id = :utilisateur AND n.sprint.id = :sprint")
    Double findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve typeNoteEleve, Integer utilisateur, Integer sprint);

    /**
     * @brief Requête pour récupérer la somme des bonus/malus des étudiants d'un étudiant en fonction du sprint
     * @param utilisateur : id de l'étudiant
     * @param sprint : numéro du sprint
     * @return Somme des bonus/malus
     */
    @Query("SELECT SUM(n.note) FROM NoteEleve n WHERE n.typeNoteEleve = 'SS_BM' AND n.eleve.id = :utilisateur AND n.sprint.id = :sprint")
    Double findSSBMByUtilisateurAndSprint(Integer utilisateur, Integer sprint);

    /**
     * @brief Requête pour récupérer la somme des bonus/malus des étudiants d'un étudiant en fonction du sprint
     * @param utilisateur : id de l'étudiant
     * @param sprint : numéro du sprint
     * @return Somme des bonus/malus
     */
    @Query("SELECT SUM(n.note) FROM NoteEleve n WHERE n.typeNoteEleve = 'TE_BM' AND n.eleve.id = :utilisateur AND n.sprint.id = :sprint")
    Double findTEBMByUtilisateurAndSprint(Integer utilisateur, Integer sprint);

    /**
     * @brief Requête pour récupérer les bonus/malus d'un étudiant en fonction du sprint (TE_BM)
     * @param utilisateurId : id de l'étudiant
     * @param sprintId : numéro du sprint
     * @return Liste des bonus/malus
     */
    @Query("SELECT n FROM NoteEleve n WHERE n.eleve.id = :utilisateurId AND n.sprint.id = :sprintId AND n.typeNoteEleve = 'TE_BM'")
    List<NoteEleve> findTe_BMbyUtilisateurAndSprint(Integer utilisateurId, Integer sprintId);

    /**
     * @brief Requête pour récupérer les bonus/malus d'un étudiant en fonction du sprint (SS_BM)
     * @param utilisateurId : id de l'étudiant
     * @param sprintId : numéro du sprint
     * @return Liste des bonus/malus
     */
    @Query("SELECT n FROM NoteEleve n WHERE n.eleve.id = :utilisateurId AND n.sprint.id = :sprintId AND n.typeNoteEleve = 'SS_BM'")
    List<NoteEleve> findSs_BMbyUtilisateurAndSprint(Integer utilisateurId, Integer sprintId);

    /**
     * @brief Requête pour récupérer les bonus/malus d'un étudiant en fonction du sprint
     * @param utilisateurId : id de l'étudiant
     * @param sprintId : numéro du sprint
     * @return Liste des bonus/malus
     */
    @Query("SELECT n FROM NoteEleve n WHERE n.typeNoteEleve = :typeNoteEleve AND n.sprint.id = :sprintId AND n.eleve.id = :utilisateurId")
    List<NoteEleve> findNoteEleveByTypeAndUser(TypeNoteEleve typeNoteEleve, Integer sprintId, Integer utilisateurId);

    @Query("SELECT n FROM NoteEleve n WHERE n.eleve.id = :idEleve AND n.sprint.id = :sprint AND n.evaluateur.id = :idEvaluateur")
    List<NoteEleve> getNotesEleves(Integer idEleve, int sprint, Integer idEvaluateur);

    @Query ("SELECT n FROM NoteEleve n WHERE n.eleve.id = :idEleve AND n.sprint.id = :sprint AND n.evaluateur.id = :idEvaluateur AND n.typeNoteEleve = :type")
    Optional<NoteEleve> findNoteEleveByEleveIdAndEvaluateurIdAndSprintId(Integer idEleve, Integer idEvaluateur, Integer sprint, TypeNoteEleve type);
}
