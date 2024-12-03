package fr.eseo.backendalphaplan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.eseo.backendalphaplan.model.BonusMalus;

/**
 * @file BonusMalusRepository.java
 * @brief Interface repository pour les BonusMalus
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface BonusMalusRepository extends JpaRepository<BonusMalus, Integer> {

    /**
     * @brief Requête pour récupérer les BonusMalus d'une équipe pour un sprint donné
     * @param id : id de l'équipe
     * @param sprint : numéro du sprint
     * @return Liste des BonusMalus
     */
    @Query("SELECT b FROM BonusMalus b WHERE b.noteEleve.eleve.equipe.id = :id AND b.noteEleve.sprint.id = :sprint")
    List<BonusMalus> getBonusMalusSprint(Integer id, int sprint);

    /**
     * @brief Requête pour récupérer une note via son id
     * @param id : id de la note
     * @return BonusMalus
     */
    @Query("SELECT b FROM BonusMalus b WHERE b.noteEleve.id = :id")
    BonusMalus findByNoteId(Integer id);
}
