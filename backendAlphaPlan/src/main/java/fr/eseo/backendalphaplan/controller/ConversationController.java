package fr.eseo.backendalphaplan.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eseo.backendalphaplan.model.Conversation;
import fr.eseo.backendalphaplan.model.Message;
import fr.eseo.backendalphaplan.services.ConversationService;

/**
 * @file ConversationController.java
 * @brief La classe ConversationController est le controller qui permet de gérer les requêtes HTTP concernant les conversations
 * @version 1.0
 * @date 24/05/2024
 * @autor Yann LIDEC
 */
@RestController
@RequestMapping("/api/conversation")
public class ConversationController {

    // Attributs
    private final ConversationService conversationService;

    // Constructeur
    @Autowired
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    /**
     * @brief Méthode permettant de récupérer toutes les conversations
     * @return Liste des conversations
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @GetMapping
    public List<Conversation> getConvs() {
    	return conversationService.getConvs();
    }

    /**
     * @brief Méthode permettant de récupérer les messages d'une conversation
     * @param conv : conversation dont on veut les messages
     * @return Liste des messages de la conversation
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @GetMapping("/{conv}")
    public List<Message> getMessages(@PathVariable Conversation conv) {
    	return conversationService.getMessagesConv(conv);
    }

    /**
     * @brief Méthode permettant de récupérer les conversations d'un utilisateur
     * @param id : id de l'utilisateur dont on veut les conversations
     * @return Liste des conversations de l'utilisateur
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<List<Conversation>> getConvsByUtilisateur(@PathVariable Integer id) {
        try {
            List<Conversation> conversations = conversationService.getConvsByUtilisateur(id);
            if (conversations.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(conversations);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @brief Méthode permettant de créer une conversation
     * @param conv : conversation à créer
     * @return La conversation en question
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @PostMapping("/creer")
    public ResponseEntity<ConversationResponse> creerConv(@RequestBody ConversationRequest conv) {
        try {
            Conversation conversation = conversationService.creerConv(conv);
            return ResponseEntity.ok(ConversationResponse.builder().conversation(conversation).message("Conversation créée").build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ConversationResponse.builder().message(e.getMessage()).build());
        }
    }

    /**
     * @brief Méthode permettant de supprimer une conversation
     * @param conversationId : id de la conversation à supprimer
     * @return Validation de la suppression de la conversation
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@PathVariable Integer conversationId) {
        conversationService.deleteConversation(conversationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * @brief Méthode permettant de supprimer un message
     * @param messageId : id du message à supprimer
     * @return Validation de la suppression du message
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @DeleteMapping("/message/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer messageId) {
        conversationService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    /**
     * @brief Méthode permettant de répondre à une conversation
     * @param conversationId : id de la conversation à laquelle on répond
     * @param message : message à ajouter
     * @return Le message ajouté
     * @autor Yann LIDEC
     * @date 24/05/2024
     */
    @PostMapping("/{conversationId}/repondre")
    public Message addMessage(@PathVariable Integer conversationId, @RequestBody Message message) {
        return conversationService.addMessage(conversationId, message);
    }

    /**
     * @brief Classe interne permettant de définir une requête de conversation.
     * @autor Hugo BOURDAIS
     * @date 03/06/2024
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static final class ConversationRequest {
        private String titre;
        private Integer auteur;
        private List<Integer> participants;
    }

    /**
     * @brief Classe interne permettant de définir une réponse de conversation.
     * @autor Hugo BOURDAIS
     * @date 03/06/2024
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Builder
    public static final class ConversationResponse {
        private Conversation conversation;
        private String message;
    }
}
