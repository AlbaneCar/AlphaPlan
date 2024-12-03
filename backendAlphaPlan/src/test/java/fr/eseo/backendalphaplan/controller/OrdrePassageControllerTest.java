package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.services.OrdrePassageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class OrdrePassageControllerTest {

    @Mock
    private OrdrePassageService ordrePassageService;

    private OrdrePassageController ordrePassageController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ordrePassageController = new OrdrePassageController(ordrePassageService);
    }

    @Test
    void getOrdrePassageByEquipeIdAndSprintIdReturnsOkWhenOrdrePassageExists() {
        when(ordrePassageService.getOrdrePassageByEquipeIdAndSprintId(anyInt(), anyInt())).thenReturn(new OrdrePassageController.OrdrePassageResponse());

        ResponseEntity<OrdrePassageController.OrdrePassageResponse> result = ordrePassageController.getOrdrePassageByEquipeIdAndSprintId(1, 1);

        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void getOrdrePassageByEquipeIdAndSprintIdReturnsBadRequestWhenOrdrePassageDoesNotExist() {
        when(ordrePassageService.getOrdrePassageByEquipeIdAndSprintId(anyInt(), anyInt())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<OrdrePassageController.OrdrePassageResponse> result = ordrePassageController.getOrdrePassageByEquipeIdAndSprintId(1, 1);

        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    void createOrdrePassageReturnsOkWhenOrdrePassageIsCreated() {
        ResponseEntity<String> result = ordrePassageController.createOrdrePassage(1, 1, Arrays.asList(1, 2, 3), 1);

        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testCreateOrdrePassageException() {
        // Prepare test data
        Integer sprintId = 1;
        Integer equipeId = 1;
        List<Integer> ordre = Arrays.asList(1, 2, 3);
        Integer auteurId = 1;

        // Simulate an IllegalArgumentException when createOrdrePassage is called
        doThrow(new IllegalArgumentException("Test exception"))
                .when(ordrePassageService)
                .createOrdrePassage(anyInt(), anyInt(), any(), anyInt());

        // Call the method
        ResponseEntity<String> result = ordrePassageController.createOrdrePassage(sprintId, equipeId, ordre, auteurId);

        // Verify the results
        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Test exception", result.getBody());
    }
}

class OrdrePassageResponseTest {

    @Test
    void testOrdrePassageResponse() {
        // Prepare test data
        Integer id = 1;
        Equipe equipe = new Equipe();
        Sprint sprint = new Sprint();
        Utilisateur auteur = new Utilisateur();
        String dateCreation = "24/05/2024";
        List<Utilisateur> ordre = new ArrayList<>();
        String error = "Test error";

        // Create an instance of OrdrePassageResponse using the all-args constructor
        OrdrePassageController.OrdrePassageResponse response = new OrdrePassageController.OrdrePassageResponse(id, equipe, sprint, auteur, dateCreation, ordre, error);

        // Verify the results
        assertEquals(id, response.getId());
        assertEquals(equipe, response.getEquipe());
        assertEquals(sprint, response.getSprint());
        assertEquals(auteur, response.getAuteur());
        assertEquals(dateCreation, response.getDateCreation());
        assertEquals(ordre, response.getOrdre());
        assertEquals(error, response.getError());
    }

    @Test
    void testRequiredArgsConstructor() {
        // Create an instance of OrdrePassageResponse using the no-args constructor
        OrdrePassageController.OrdrePassageResponse response = new OrdrePassageController.OrdrePassageResponse();

        // Verify the results
        assertNotNull(response);
    }

    @Test
    void testDataAnnotation() {
        // Prepare test data
        Integer id = 1;
        Equipe equipe = new Equipe();
        Sprint sprint = new Sprint();
        Utilisateur auteur = new Utilisateur();
        String dateCreation = "24/05/2024";
        List<Utilisateur> ordre = new ArrayList<>();
        String error = "Test error";

        // Create an instance of OrdrePassageResponse using the no-args constructor
        OrdrePassageController.OrdrePassageResponse response = new OrdrePassageController.OrdrePassageResponse();

        // Set the fields
        response.setId(id);
        response.setEquipe(equipe);
        response.setSprint(sprint);
        response.setAuteur(auteur);
        response.setDateCreation(dateCreation);
        response.setOrdre(ordre);
        response.setError(error);

        // Verify the results
        assertEquals(id, response.getId());
        assertEquals(equipe, response.getEquipe());
        assertEquals(sprint, response.getSprint());
        assertEquals(auteur, response.getAuteur());
        assertEquals(dateCreation, response.getDateCreation());
        assertEquals(ordre, response.getOrdre());
        assertEquals(error, response.getError());
    }
}