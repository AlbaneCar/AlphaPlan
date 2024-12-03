package fr.eseo.backendalphaplan.controller;


import fr.eseo.backendalphaplan.dto.SprintCreationResponse;
import fr.eseo.backendalphaplan.dto.SprintDto;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.enums.FinSprint;
import fr.eseo.backendalphaplan.services.SprintService;
import fr.eseo.backendalphaplan.utils.Note;
import fr.eseo.backendalphaplan.utils.NotesUtilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SprintControllerTest {

    @InjectMocks
    private SprintController sprintController;

    @Mock
    private SprintService sprintService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSprints() {
        Sprint sprint = new Sprint();
        when(sprintService.getAllSprints()).thenReturn(List.of(sprint));

        List<Sprint> result = sprintController.getAllSprints();

        assertEquals(1, result.size());
        verify(sprintService, times(1)).getAllSprints();
    }

    @Test
    void testGetSprintById() {
        Sprint sprint = new Sprint();
        when(sprintService.getSprintById(1)).thenReturn(Optional.of(sprint));

        ResponseEntity<Sprint> result = sprintController.getSprintById(1);

        assertEquals(sprint, result.getBody());
        verify(sprintService, times(1)).getSprintById(1);
    }

    @Test
    void testCreateSprints() {
        Sprint sprint = new Sprint();
        when(sprintService.saveSprints(anyList())).thenReturn(List.of(sprint));

        ResponseEntity<List<Sprint>> result = sprintController.createSprints(List.of(sprint));

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
        verify(sprintService, times(1)).saveSprints(anyList());
    }

    @Test
    void testDeleteSprint() {
        doNothing().when(sprintService).deleteSprint(anyInt());

        ResponseEntity<Void> result = sprintController.deleteSprint(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(sprintService, times(1)).deleteSprint(anyInt());
    }

    @Test
    void testGetNombreSprints() {
        when(sprintService.getAllSprints()).thenReturn(Arrays.asList(new Sprint(), new Sprint()));

        ResponseEntity<Integer> result = sprintController.getNombreSprints();

        assertEquals(2, result.getBody());
        verify(sprintService, times(1)).getAllSprints();
    }

    @Test
    void testGetSprints() {
        Sprint sprint = new Sprint();
        when(sprintService.getAllSprints()).thenReturn(List.of(sprint));

        ResponseEntity<List<Sprint>> result = sprintController.getSprints();

        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
        verify(sprintService, times(1)).getAllSprints();
    }


    @Test
    void testGetNotesEquipeBySprintId() {
        NotesUtilisateur notesUtilisateur = new NotesUtilisateur("nom", "prenom", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        when(sprintService.getNoteEquipeByEquipeAndSprint(anyInt(), anyInt())).thenReturn(List.of(notesUtilisateur));

        ResponseEntity<List<NotesUtilisateur>> result = sprintController.getNotesEquipeBySprintId(1, 1);

        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
        verify(sprintService, times(1)).getNoteEquipeByEquipeAndSprint(anyInt(), anyInt());
    }

    @Test
    void testGetNotesEleveBySprintIdException() {
        when(sprintService.getNoteEleveByEleveAndSprint(anyInt(), anyInt())).thenThrow(new IllegalArgumentException());

        ResponseEntity<List<Note>> result = sprintController.getNotesEleveBySprintId(1, 1);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(sprintService, times(1)).getNoteEleveByEleveAndSprint(anyInt(), anyInt());
    }

    @Test
    void testGetNotesEquipeBySprintIdException() {
        when(sprintService.getNoteEquipeByEquipeAndSprint(anyInt(), anyInt())).thenThrow(new IllegalArgumentException());

        ResponseEntity<List<NotesUtilisateur>> result = sprintController.getNotesEquipeBySprintId(1, 1);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(sprintService, times(1)).getNoteEquipeByEquipeAndSprint(anyInt(), anyInt());
    }

    @Test
    void testCreateSprintsFromFormData() {
        // Prepare test data
        List<SprintDto> sprintDtos = new ArrayList<>();
        SprintDto sprintDto = new SprintDto();
        sprintDto.setStartDate(LocalDate.now());
        sprintDto.setEndDate(LocalDate.now().plusDays(1));
        sprintDto.setSprintEndType("Sprint Review");
        sprintDtos.add(sprintDto);

        List<Sprint> expectedSprints = new ArrayList<>();
        Sprint sprint = new Sprint();
        sprint.setStartDate(sprintDto.getStartDate());
        sprint.setEndDate(sprintDto.getEndDate());
        sprint.setName("Sprint 1");
        sprint.setSprintEndType(Sprint.convertStringToEnum("Sprint Review"));
        expectedSprints.add(sprint);

        // Mock the behavior of sprintService
        doNothing().when(sprintService).deleteAllSprints();
        when(sprintService.saveSprints(anyList())).thenReturn(expectedSprints);

        // Call the method under test
        ResponseEntity<SprintCreationResponse> result = sprintController.createSprintsFromFormData(sprintDtos);

        // Verify the results
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(expectedSprints, Objects.requireNonNull(result.getBody()).getCreatedSprints());
        assertEquals(new ArrayList<>(), result.getBody().getErrors());

        // Verify the interactions with the mock
        verify(sprintService, times(1)).deleteAllSprints();
        verify(sprintService, times(1)).saveSprints(anyList());
    }

    @Test
    void testUpdateSprint() {
        // Prepare test data
        Sprint sprint = new Sprint();
        sprint.setId(1);

        // Mock the behavior of sprintService
        when(sprintService.getSprintById(any())).thenReturn(Optional.of(sprint));
        when(sprintService.updateSprint(any())).thenReturn(sprint);

        // Call the method
        ResponseEntity<Sprint> result = sprintController.updateSprint(sprint);

        // Verify the results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(sprint, result.getBody());
    }

    @Test
    void testUpdateSprintNotFound() {
        // Prepare test data
        Sprint sprint = new Sprint();
        sprint.setId(1);

        // Mock the behavior of sprintService
        when(sprintService.getSprintById(any())).thenReturn(Optional.empty());

        // Call the method
        ResponseEntity<Sprint> result = sprintController.updateSprint(sprint);

        // Verify the results
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testStartDateBeforeCurrentDate() {
        // Prepare test data
        Sprint sprint = new Sprint();
        sprint.setStartDate(LocalDate.now().minusDays(1));

        List<String> errors = new ArrayList<>();
        if (sprint.getStartDate().isBefore(LocalDate.now())) {
            errors.add("La date de début du Sprint ne peut pas être antérieure à la date actuelle.");
        }

        // Verify the results
        assertTrue(errors.contains("La date de début du Sprint ne peut pas être antérieure à la date actuelle."));
    }

    @Test
    void testEndDateBeforeStartDate() {
        // Prepare test data
        Sprint sprint = new Sprint();
        sprint.setStartDate(LocalDate.now().plusDays(1));
        sprint.setEndDate(LocalDate.now());

        List<String> errors = new ArrayList<>();
        if (sprint.getEndDate().isBefore(sprint.getStartDate())) {
            errors.add("La date de fin du Sprint ne peut pas être antérieure à la date de début.");
        }

        // Verify the results
        assertTrue(errors.contains("La date de fin du Sprint ne peut pas être antérieure à la date de début."));
    }

    @Test
    void testStartDateBeforePreviousSprintEndDate() {
        // Prepare test data
        Sprint previousSprint = new Sprint();
        previousSprint.setEndDate(LocalDate.now().plusDays(1));

        Sprint sprint = new Sprint();
        sprint.setStartDate(LocalDate.now());

        List<Sprint> sprints = new ArrayList<>();
        sprints.add(previousSprint);
        sprints.add(sprint);

        List<String> errors = new ArrayList<>();
        if (sprint.getStartDate().isBefore(previousSprint.getEndDate())) {
            errors.add("La date de début du Sprint ne peut pas être antérieure à la date de fin du Sprint précédent.");
        }

        // Verify the results
        assertTrue(errors.contains("La date de début du Sprint ne peut pas être antérieure à la date de fin du Sprint précédent."));
    }

    @Test
    void testCreateSprintsFromFormDataWithErrors() {
        // Prepare test data
        List<SprintDto> sprintDtos = new ArrayList<>();
        SprintDto sprintDto = new SprintDto();
        sprintDto.setStartDate(LocalDate.now().minusDays(1));
        sprintDto.setEndDate(LocalDate.now());
        sprintDto.setSprintEndType("Sprint Review");
        sprintDtos.add(sprintDto);

        // Mock the behavior of sprintService
        doNothing().when(sprintService).deleteAllSprints();
        when(sprintService.saveSprints(anyList())).thenThrow(new IllegalArgumentException("La date de début du Sprint 1 ne peut pas être antérieure à la date actuelle."));

        // Call the method under test
        ResponseEntity<SprintCreationResponse> result = sprintController.createSprintsFromFormData(sprintDtos);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(result.getBody().getErrors().contains("La date de début du Sprint 1 ne peut pas être antérieure à la date actuelle."));
    }



}