package fr.eseo.backendalphaplan.services;

import java.util.List;
import java.util.Optional;

import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eseo.backendalphaplan.model.Conversation;
import fr.eseo.backendalphaplan.model.Message;
import fr.eseo.backendalphaplan.repository.ConversationRepository;
import fr.eseo.backendalphaplan.repository.MessageRepository;
import fr.eseo.backendalphaplan.controller.ConversationController.ConversationRequest;

/**
 * @file ConversationService.java
 * @brief Service pour les Conversations
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
public class ConversationService {

    // Attributs
	private final ConversationRepository conversationRepository;
	private final MessageRepository messageRepository;
    private static final String ERROR_MESSAGE = "Conversation not found";
    private final UtilisateurRepository utilisateurRepository;

    // Constructeur
    @Autowired
    public ConversationService(ConversationRepository conversationRepository, MessageRepository messageRepository, UtilisateurRepository utilisateurRepository) {
    	this.conversationRepository = conversationRepository;
    	this.messageRepository = messageRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * @brief Récupérer toutes les conversations
     * @return Conversation
     */
    public List<Conversation> getConvs() {
        return conversationRepository.findAll();
    }

    /**
     * @brief Récupérer les messages d'une conversation
     * @param conv : conversation
     * @return Liste des messages
     */
    public List<Message> getMessagesConv(Conversation conv) {
        return messageRepository.findByConversation(conv);
    }

    /**
     * @brief Permet de créer une conversation
     * @param conv : conversation
     * @return Conversation créée
     */
    public Conversation creerConv(ConversationRequest conv) throws  IllegalArgumentException {
        // On vérifie que la conversation n'existe pas déjà
        if (conversationRepository.findByTitre(conv.getTitre()).isPresent()) {
            throw new IllegalArgumentException("Une conversation avec ce titre existe déjà");
        }
        // On vérifie que l'auteur existe
        Optional<Utilisateur> auteur = utilisateurRepository.findById(conv.getAuteur());
        if (auteur.isEmpty()) {
            throw new IllegalArgumentException("L'auteur n'existe pas");
        }
        // On vérifie qu'il y a au moins un participant
        if (conv.getParticipants().isEmpty() || conv.getParticipants().size() < 2) {
            throw new IllegalArgumentException("Il faut au moins un participant en plus de l'auteur");
        }
        // On vérifie que les participants existent
        for (Integer id : conv.getParticipants()) {
            if (!utilisateurRepository.existsById(id)) {
                throw new IllegalArgumentException("Un des participants n'existe pas");
            }
        }
        // On crée la conversation
        Conversation conversation = new Conversation();
        conversation.setTitre(conv.getTitre());
        conversation.setAuteur(auteur.get());
        conversation.setParticipants(utilisateurRepository.findAllById(conv.getParticipants()));
        // On sauvegarde la conversation
        return conversationRepository.save(conversation);
    }

    /**
     * @brief Permet de supprimer une conversation
     * @param conversationId : id de la conversation
     */
    public void deleteConversation(Integer conversationId) throws NullPointerException {
        if (!conversationRepository.existsById(conversationId)) {
            throw new NullPointerException(ERROR_MESSAGE);
        }
        
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NullPointerException(ERROR_MESSAGE));
        List<Message> messages = messageRepository.findByConversation(conversation);
        messageRepository.deleteAll(messages);

        conversationRepository.deleteById(conversationId);
    }

    /**
     * @brief Permet de supprimer un message
     * @param messageId : id du message
     */
    public void deleteMessage(Integer messageId) throws NullPointerException {
        if (!messageRepository.existsById(messageId)) {
            throw new NullPointerException("Message not found");
        }
        messageRepository.deleteById(messageId);
    }

    /**
     * @brief Permet d'ajouter un message à une conversation
     * @param conversationId : id de la conversation
     * @param message : message
     * @return Message ajouté
     */
    public Message addMessage(Integer conversationId, Message message) throws NullPointerException {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NullPointerException(ERROR_MESSAGE));
        message.setConversation(conversation);
        return messageRepository.save(message);
    }

    /**
     * @brief Permet de récupérer les conversations d'un utilisateur
     * @param id : id de l'utilisateur
     * @return Liste des conversations
     */
    public List<Conversation> getConvsByUtilisateur(Integer id) throws NullPointerException {
        // On vérifie que l'utilisateur existe
        if (!utilisateurRepository.existsById(id)) {
            throw new NullPointerException("Utilisateur non trouvé");
        }
        return conversationRepository.findByParticipantsId(id);
    }
}
