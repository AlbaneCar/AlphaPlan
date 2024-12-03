package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.OrdrePassage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @file OrdrePassageRepository.java
 * @brief Interface repository pour les OrdrePassage
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface OrdrePassageRepository extends JpaRepository<OrdrePassage, Integer> {

    /**
     * @brief Requête pour récupérer l'ordre de passage d'une équipe pour un sprint donné
     * @param equipeId : id de l'équipe
     * @param sprintId : id du sprint
     * @return OrdrePassage
     */
    @Query("SELECT o FROM OrdrePassage o WHERE o.equipe.id = :equipeId AND o.sprint.id = :sprintId")
    OrdrePassage findByEquipeIdAndSprintId(Integer equipeId, Integer sprintId);
}
