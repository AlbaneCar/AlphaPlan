package fr.eseo.backendalphaplan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eseo.backendalphaplan.model.Conversation;
import fr.eseo.backendalphaplan.model.Message;

/**
 * @file MessageRepository.java
 * @brief Interface repository pour les Messages
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {

    /**
     * @brief Requête pour récupérer les messages d'une conversation
     * @param conv : conversation
     * @return Liste des messages
     */
    List<Message> findByConversation(Conversation conv);
}
