package fr.eseo.backendalphaplan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eseo.backendalphaplan.model.Conversation;

/**
 * @file ConversationRepository.java
 * @brief Interface repository pour les Conversations
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    /**
     * @brief Requête pour récupérer une conversation via son titre
     * @param titre : titre de la conversation
     * @return Conversation
     */
    Optional<Conversation> findByTitre(String titre);

    /**
     * @brief Requête pour récupérer les Conversations d'un utilisateur
     * @param id : id de l'utilisateur
     * @return Liste des Conversations
     */
    List<Conversation> findByParticipantsId(Integer id);
}
