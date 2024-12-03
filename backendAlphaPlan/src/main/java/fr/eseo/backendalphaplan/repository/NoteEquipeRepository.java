package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.enums.TypeNoteEquipe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import fr.eseo.backendalphaplan.model.NoteEquipe;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;

/**
 * @file NoteEquipeRepository.java
 * @brief Interface repository pour les NoteEquipe
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface NoteEquipeRepository extends JpaRepository<NoteEquipe, Integer> {

    /**
     * @brief Requête pour la moyenne des notes d'une équipe pour un sprint donné et un type de note donné
     * @param typeNoteEquipe : type de note
     * @param equipe : id de l'équipe
     * @param sprint : numéro du sprint
     * @return Moyenne des notes
     */
    @Query("SELECT AVG(n.note) FROM NoteEquipe n WHERE n.typeNoteEquipe = :typeNoteEquipe AND n.equipe.id = :equipe AND n.sprint.id = :sprint")
    Double findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe typeNoteEquipe, Integer equipe, Integer sprint);

    /**
     * @brief Requête pour récupérer les notes d'une équipe pour un sprint donné
     * @param id : id de l'équipe
     * @param sprint : numéro du sprint
     * @return Liste des notes
     */
    @Query("SELECT n FROM NoteEquipe n WHERE n.equipe.id = :id AND n.sprint.id = :sprint")
    List<NoteEquipe> getNotesSprint(Integer id, int sprint);

    /**
     * @brief Requête pour récupérer la moyenne des notes d'une équipe pour un sprint donné
     * @param id : id de l'équipe
     * @param sprint : numéro du sprint
     * @return Moyenne des notes
     */
    @Query("SELECT AVG(n.note) FROM NoteEquipe n WHERE n.equipe.id = :id AND n.sprint.id = :sprint")
    double getMoyenneSprint(Integer id, int sprint);

    @Query("SELECT AVG(n.note) FROM NoteEquipe n WHERE n.equipe.id = :id AND n.sprint.id = :sprint")
    Optional<Double> getMoyenneSprintOptional(Integer id, int sprint);

    /**
     * @brief Requête pour récupérer la note d'une équipe pour un sprint donné et un type de note donné
     * @param id : id de l'équipe
     * @param sprint : numéro du sprint
     * @param type : type de note
     * @return Note
     */
    @Query("SELECT n FROM NoteEquipe n WHERE n.equipe.id = :id AND n.sprint.id = :sprint AND n.typeEchelle = :type")
    NoteEquipe getNoteType(Integer id, int sprint, TypeEchelle type);

    /**
     * @brief Requête pour récupérer une note via l'id de l'équipe, l'id de l'évaluateur, l'id du sprint et le type de note
     * @param equipeId : id de l'équipe
     * @param evaluateurId : id de l'évaluateur
     * @param sprintId : id du sprint
     * @param typeNoteEquipe : type de note
     * @return NoteEquipe
     */
    @Query("SELECT n FROM NoteEquipe n WHERE n.equipe.id = :equipeId AND n.evaluateur.id = :evaluateurId AND n.sprint.id = :sprintId AND n.typeNoteEquipe = :typeNoteEquipe")
    Optional<NoteEquipe> findNoteEquipeByEquipeIdAndEvaluateurIdAndSprintId(Integer equipeId, Integer evaluateurId, Integer sprintId, TypeNoteEquipe typeNoteEquipe);


    @Query("SELECT n FROM NoteEquipe n WHERE n.equipe.id = :equipeId AND n.sprint.id = :sprintId AND n.evaluateur.id = :evaluateurId")
    List<NoteEquipe> findNoteEquipeByEquipeIdAndSprintId(Integer equipeId, Integer sprintId, Integer evaluateurId);
    /**
     * @brief Requête pour récupérer les notes d'une équipe pour un sprint donné
     * @param equipeId : id de l'équipe
     * @param sprintId : id du sprint
     * @return Liste des notes
     */
    @Query("SELECT n FROM NoteEquipe n WHERE n.equipe.id = :equipeId AND n.sprint.id = :sprintId")
    List<NoteEquipe> findNoteEquipeByEquipeIdAndSprintId(Integer equipeId, Integer sprintId);

    /**
     * @brief Requête pour récupérer les notes d'une équipe pour un sprint donné et un type de note donné
     * @param typeNoteEquipe : type de note
     * @param sprintId : id du sprint
     * @param equipeId : id de l'équipe
     * @return Liste des notes
     */
    @Query("SELECT n FROM NoteEquipe n WHERE n.equipe.id = :equipeId AND n.sprint.id = :sprintId AND n.typeNoteEquipe = :typeNoteEquipe")
    List<NoteEquipe> findNotesByEquipeAndSprintAndType(TypeNoteEquipe typeNoteEquipe, Integer sprintId, Integer equipeId);
}
