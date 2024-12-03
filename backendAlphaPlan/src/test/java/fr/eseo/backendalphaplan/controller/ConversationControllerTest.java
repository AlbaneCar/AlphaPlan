package fr.eseo.backendalphaplan.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.eseo.backendalphaplan.model.Conversation;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Message;
import fr.eseo.backendalphaplan.services.ConversationService;

@ExtendWith(MockitoExtension.class)
class ConversationControllerTest {

    @Mock
    private ConversationService conversationService;

    @InjectMocks
    private ConversationController conversationController;

    private List<Conversation> conversationList;
    private List<Message> messageList;
    private Equipe equipe;

    @BeforeEach
    void setUp() {
        conversationList = new ArrayList<>();
        messageList = new ArrayList<>();
        equipe = new Equipe();
        Conversation conv = new Conversation();
        conv.setId(1);
        conversationList.add(conv);
        Message message = new Message();
        message.setId(1);
        messageList.add(message);
    }

    @Test
    void testGetConvs() {
        when(conversationService.getConvs()).thenReturn(conversationList);
        List<Conversation> result = conversationController.getConvs();
        assertEquals(1, result.size());
        assertEquals(conversationList, result);
    }

    @Test
    void testDeleteConversation() {
        int id = 1;
        doNothing().when(conversationService).deleteConversation(id);
        ResponseEntity<Void> response = conversationController.deleteConversation(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testConversationRequest() {
        // Arrange
        String expectedTitre = "Test Title";
        Integer expectedAuteur = 1;
        List<Integer> expectedParticipants = Arrays.asList(1, 2, 3);

        // Act
        ConversationController.ConversationRequest request = new ConversationController.ConversationRequest(expectedTitre, expectedAuteur, expectedParticipants);

        // Assert
        assertEquals(expectedTitre, request.getTitre());
        assertEquals(expectedAuteur, request.getAuteur());
        assertEquals(expectedParticipants, request.getParticipants());
    }


    @Test
    void getMessagesReturnsExpectedList() {
        // Arrange
        Conversation conversation = new Conversation();
        Message message1 = new Message();
        Message message2 = new Message();
        List<Message> expectedMessages = Arrays.asList(message1, message2);

        when(conversationService.getMessagesConv(conversation)).thenReturn(expectedMessages);

        // Act
        List<Message> actualMessages = conversationController.getMessages(conversation);

        // Assert
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    void testDeleteMessage() {
        int id = 1;
        doNothing().when(conversationService).deleteMessage(id);
        ResponseEntity<Void> response = conversationController.deleteMessage(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testAddMessage() {
        Message messageToAdd = new Message();
        messageToAdd.setId(2);
        when(conversationService.addMessage(anyInt(), any(Message.class))).thenReturn(messageToAdd);
        Message result = conversationController.addMessage(1, messageToAdd);
        assertEquals(messageToAdd, result);
    }

    @Test
    void getConvsByUtilisateurReturnsExpectedList() {
        when(conversationService.getConvsByUtilisateur(anyInt())).thenReturn(conversationList);

        ResponseEntity<List<Conversation>> result = conversationController.getConvsByUtilisateur(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(conversationList, result.getBody());
    }

    @Test
    void getConvsByUtilisateurReturnsNoContentWhenListIsEmpty() {
        when(conversationService.getConvsByUtilisateur(anyInt())).thenReturn(new ArrayList<>());

        ResponseEntity<List<Conversation>> result = conversationController.getConvsByUtilisateur(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void getConvsByUtilisateurReturnsBadRequestWhenServiceThrowsException() {
        when(conversationService.getConvsByUtilisateur(anyInt())).thenThrow(NullPointerException.class);

        ResponseEntity<List<Conversation>> result = conversationController.getConvsByUtilisateur(1);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void creerConvReturnsExpectedResponse() {
        ConversationController.ConversationRequest request = new ConversationController.ConversationRequest("title", 1, List.of(1, 2, 3));
        Conversation conversation = new Conversation();
        when(conversationService.creerConv(any())).thenReturn(conversation);

        ResponseEntity<ConversationController.ConversationResponse> result = conversationController.creerConv(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(conversation, result.getBody().getConversation());
        assertEquals("Conversation créée", result.getBody().getMessage());
    }

    @Test
    void creerConvReturnsBadRequestWhenServiceThrowsException() {
        ConversationController.ConversationRequest request = new ConversationController.ConversationRequest("title", 1, List.of(1, 2, 3));
        when(conversationService.creerConv(any())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<ConversationController.ConversationResponse> result = conversationController.creerConv(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(null, result.getBody().getMessage());
    }

    @Test
    void addMessageReturnsExpectedMessage() {
        Message message = new Message();
        when(conversationService.addMessage(anyInt(), any())).thenReturn(message);

        Message result = conversationController.addMessage(1, message);

        assertEquals(message, result);
    }

    @Test
    void testConstructorAndGetters() {
        String titre = "Test title";
        Integer auteur = 1;
        List<Integer> participants = new ArrayList<>(Arrays.asList(1, 2, 3));
        ConversationController.ConversationRequest conversationRequest = new ConversationController.ConversationRequest(titre, auteur, participants);

        assertEquals(titre, conversationRequest.getTitre());
        assertEquals(auteur, conversationRequest.getAuteur());
        assertEquals(participants, conversationRequest.getParticipants());
    }

    @Test
    void testNoArgConstructor() {
        ConversationController.ConversationRequest conversationRequest = new ConversationController.ConversationRequest();

        assertNotNull(conversationRequest);
    }


    @Test
    void testConstructorAndGetters2() {
        Conversation conversation = new Conversation();
        String message = "Test message";
        ConversationController.ConversationResponse conversationResponse = new ConversationController.ConversationResponse(conversation, message);

        assertEquals(conversation, conversationResponse.getConversation());
        assertEquals(message, conversationResponse.getMessage());
    }

    @Test
    void testNoArgConstructor2() {
        ConversationController.ConversationResponse conversationResponse = new ConversationController.ConversationResponse();

        assertNotNull(conversationResponse);
    }

}
