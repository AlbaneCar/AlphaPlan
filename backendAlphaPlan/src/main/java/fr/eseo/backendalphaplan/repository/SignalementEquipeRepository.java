package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.SignalementEquipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @file SignalementEquipeRepository.java
 * @brief Interface repository pour les SignalementEquipe
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface SignalementEquipeRepository extends JpaRepository<SignalementEquipe, Integer> {

    /**
     * @brief Requête pour récupérer les signalements d'une équipe pour un utilisateur donné
     * @param userId : id de l'utilisateur
     * @param equipeId : id de l'équipe
     * @return Liste des SignalementEquipe
     */
    List<SignalementEquipe> findByUtilisateurIdAndEquipeId(int userId, int equipeId);
}
