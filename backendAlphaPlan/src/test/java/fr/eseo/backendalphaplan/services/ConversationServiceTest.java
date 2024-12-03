package fr.eseo.backendalphaplan.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.eseo.backendalphaplan.controller.ConversationController;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.eseo.backendalphaplan.model.Conversation;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Message;
import fr.eseo.backendalphaplan.repository.ConversationRepository;
import fr.eseo.backendalphaplan.repository.MessageRepository;

class ConversationServiceTest {

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private ConversationService conversationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetConvs() {
        List<Conversation> conversations = Arrays.asList(new Conversation(), new Conversation());
        when(conversationRepository.findAll()).thenReturn(conversations);

        List<Conversation> result = conversationService.getConvs();

        assertEquals(2, result.size());
        verify(conversationRepository, times(1)).findAll();
    }
/*
    @Test
    public void testGetMessagesConv() {
        Conversation conv = new Conversation();
        List<Message> messages = Arrays.asList(new Message(), new Message());
        when(messageRepository.findByConversation(conv)).thenReturn(messages);

        List<Message> result = conversationService.getMessagesConv(conv);

        assertEquals(2, result.size());
        verify(messageRepository, times(1)).findByConversation(conv);
    }
/*
    @Test
    public void testGetConvsByEquipe() {
        Equipe equipe = new Equipe();
        List<Conversation> conversations = Arrays.asList(new Conversation(), new Conversation());
        when(conversationRepository.findByEquipe(equipe)).thenReturn(conversations);

        List<Conversation> result = conversationService.getConvsByEquipe(equipe);

        assertEquals(2, result.size());
        verify(conversationRepository, times(1)).findByEquipe(equipe);
    }

    @Test
    public void testCreerConv() {
        Conversation conv = new Conversation();
        conv.setTitre("Titre");
        when(conversationRepository.findByTitre(conv.getTitre())).thenReturn(Optional.empty());
        when(conversationRepository.save(any(Conversation.class))).thenReturn(conv);

        Conversation result = conversationService.creerConv(conv);

        assertEquals(conv, result);
        verify(conversationRepository, times(1)).findByTitre(conv.getTitre());
        verify(conversationRepository, times(1)).save(conv);
    }
    
    @Test
    public void testCreerConvExistingTitle() {
        Conversation conv = new Conversation();
        conv.setTitre("Titre");
        when(conversationRepository.findByTitre(conv.getTitre())).thenReturn(Optional.of(conv));

        assertThrows(IllegalArgumentException.class, () -> {
            conversationService.creerConv(conv);
        });

        verify(conversationRepository, times(1)).findByTitre(conv.getTitre());
        verify(conversationRepository, never()).save(any(Conversation.class));
    }
*/
    @Test
    void testDeleteConversation() {
        Integer conversationId = 1;
        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        List<Message> messages = Arrays.asList(new Message(), new Message());

        when(conversationRepository.existsById(conversationId)).thenReturn(true);
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(messageRepository.findByConversation(conversation)).thenReturn(messages);

        conversationService.deleteConversation(conversationId);

        verify(conversationRepository, times(1)).existsById(conversationId);
        verify(conversationRepository, times(1)).findById(conversationId);
        verify(messageRepository, times(1)).findByConversation(conversation);
        verify(messageRepository, times(1)).deleteAll(messages);
        verify(conversationRepository, times(1)).deleteById(conversationId);
    }

    @Test
    void testDeleteConversationNotFound() {
        Integer conversationId = 1;
        when(conversationRepository.existsById(conversationId)).thenReturn(false);

        assertThrows(NullPointerException.class, () -> {
            conversationService.deleteConversation(conversationId);
        });

        verify(conversationRepository, times(1)).existsById(conversationId);
        verify(conversationRepository, never()).findById(conversationId);
        verify(messageRepository, never()).findByConversation(any(Conversation.class));
        verify(messageRepository, never()).deleteAll(anyList());
        verify(conversationRepository, never()).deleteById(conversationId);
    }

    @Test
    void testDeleteMessage() {
        Integer messageId = 1;
        when(messageRepository.existsById(messageId)).thenReturn(true);

        conversationService.deleteMessage(messageId);

        verify(messageRepository, times(1)).existsById(messageId);
        verify(messageRepository, times(1)).deleteById(messageId);
    }

    @Test
    void testDeleteMessageNotFound() {
        Integer messageId = 1;
        when(messageRepository.existsById(messageId)).thenReturn(false);

        assertThrows(NullPointerException.class, () -> {
            conversationService.deleteMessage(messageId);
        });

        verify(messageRepository, times(1)).existsById(messageId);
        verify(messageRepository, never()).deleteById(messageId);
    }

    @Test
    void testAddMessage() {
        Integer conversationId = 1;
        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        Message message = new Message();

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message result = conversationService.addMessage(conversationId, message);

        assertEquals(message, result);
        verify(conversationRepository, times(1)).findById(conversationId);
        verify(messageRepository, times(1)).save(message);
    }
    @Test
    void testGetMessagesConv() {
        Conversation conv = new Conversation();
        List<Message> messages = Arrays.asList(new Message(), new Message());
        when(messageRepository.findByConversation(conv)).thenReturn(messages);

        List<Message> result = conversationService.getMessagesConv(conv);

        assertEquals(2, result.size());
        verify(messageRepository, times(1)).findByConversation(conv);
    }

    @Test
    void testCreerConv() {
        ConversationController.ConversationRequest convRequest = new ConversationController.ConversationRequest();
        convRequest.setTitre("Titre");
        convRequest.setAuteur(1);
        convRequest.setParticipants(Arrays.asList(2, 3));

        when(conversationRepository.findByTitre(convRequest.getTitre())).thenReturn(Optional.empty());
        when(utilisateurRepository.findById(convRequest.getAuteur())).thenReturn(Optional.of(new Utilisateur()));
        when(utilisateurRepository.existsById(anyInt())).thenReturn(true);
        when(conversationRepository.save(any(Conversation.class))).thenReturn(new Conversation());

        Conversation result = conversationService.creerConv(convRequest);

        assertNotNull(result);
        verify(conversationRepository, times(1)).findByTitre(convRequest.getTitre());
        verify(conversationRepository, times(1)).save(any(Conversation.class));
    }

    @Test
    void testGetConvsByUtilisateur() {
        Integer id = 1;
        List<Conversation> conversations = Arrays.asList(new Conversation(), new Conversation());
        when(utilisateurRepository.existsById(id)).thenReturn(true);
        when(conversationRepository.findByParticipantsId(id)).thenReturn(conversations);

        List<Conversation> result = conversationService.getConvsByUtilisateur(id);

        assertEquals(2, result.size());
        verify(conversationRepository, times(1)).findByParticipantsId(id);
    }
}
