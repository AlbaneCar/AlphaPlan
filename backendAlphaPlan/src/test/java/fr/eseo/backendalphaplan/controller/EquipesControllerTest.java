package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.dto.UtilisateurDTO;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.EtatEquipes;
import fr.eseo.backendalphaplan.services.EquipeService;
import fr.eseo.backendalphaplan.services.NoteAnterieureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EquipesControllerTest {

    @InjectMocks
    private EquipesController equipesController;

    @Mock
    private EquipeService equipeService;

    @Mock
    private NoteAnterieureService noteAnterieureService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEquipes() {
        List<Equipe> expected = Arrays.asList(new Equipe(), new Equipe());
        when(equipeService.getEquipes()).thenReturn(expected);

        List<Equipe> result = equipesController.getEquipes();

        assertEquals(expected, result);
        verify(equipeService, times(1)).getEquipes();
    }

    @Test
    void testGetEquipeById() {
        Equipe expected = new Equipe();
        when(equipeService.getEquipeById(1)).thenReturn(expected);

        Equipe result = equipesController.getEquipeById(1);

        assertEquals(expected, result);
        verify(equipeService, times(1)).getEquipeById(1);
    }

    @Test
    void testCreerEquipes() {
        // Prepare data
        List<List<Utilisateur>> equipes = Arrays.asList(
                Arrays.asList(new Utilisateur(), new Utilisateur()),
                Arrays.asList(new Utilisateur(), new Utilisateur())
        );
        when(equipeService.creerEquipes(2)).thenReturn(equipes);

        // Call method
        ResponseEntity<List<List<UtilisateurDTO>>> result = equipesController.creerEquipes(2);

        // Assert response status and verify interactions
        assertEquals(200, result.getStatusCodeValue());
        verify(equipeService, times(1)).creerEquipes(2);
    }

//    @Test
//    void testHandlePostRequest() {
//        String requestBody = "{\"key\":\"value\"}";
//        doNothing().when(equipeService).processJsonAndAssignUsers(requestBody, etatEquipes);
//
//        ResponseEntity<String> result = equipesController.handlePostRequest(requestBody, etatEquipes);
//
//        assertEquals(200, result.getStatusCodeValue());
//        assertEquals("Données reçues avec succès !", result.getBody());
//        verify(equipeService, times(1)).processJsonAndAssignUsers(requestBody, etatEquipes);
//    }

    @Test
    void testGetEquipeReferent() {
        Equipe expected = new Equipe();
        when(equipeService.getEquipeReferentById(1)).thenReturn(expected);

        Equipe result = equipesController.getEquipeReferent(1);

        assertEquals(expected, result);
        verify(equipeService, times(1)).getEquipeReferentById(1);
    }


    @Test
    void testSendTeams() {
        String requestBody = "{\"key\":\"value\"}";
        EtatEquipes etatEquipes = EtatEquipes.PUBLIE; // replace with actual value
        doNothing().when(equipeService).processJsonAndAssignUsers(requestBody, etatEquipes);

        ResponseEntity<Map<String, String>> result = equipesController.sendTeams(requestBody, etatEquipes);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Données reçues avec succès !", result.getBody().get("message"));
        verify(equipeService, times(1)).processJsonAndAssignUsers(requestBody, etatEquipes);
    }

    @Test
    void testChangeTeamsState() {
        EtatEquipes etatEquipes = EtatEquipes.PUBLIE; // replace with actual value
        doNothing().when(equipeService).changeTeamsState(etatEquipes);

        ResponseEntity<Map<String, String>> result = equipesController.changeTeamsState(etatEquipes);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Données reçues avec succès !", result.getBody().get("message"));
        verify(equipeService, times(1)).changeTeamsState(etatEquipes);
    }
}