package fr.eseo.backendalphaplan.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import fr.eseo.backendalphaplan.dto.MoyenneEquipeResponse;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.NoteEquipe;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.services.EquipeService;
import fr.eseo.backendalphaplan.services.NoteEquipeService;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEquipe;
import fr.eseo.backendalphaplan.services.SprintService;
import fr.eseo.backendalphaplan.services.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

class NoteEquipeControllerTest {

    @InjectMocks
    NoteEquipeController noteEquipeController;

    @Mock
    NoteEquipeService noteEquipeService;

    @Mock
    UtilisateurService utilisateurService;

    @Mock
    SprintService sprintService;

    @Mock
    EquipeService equipeService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void testGetNotesEquipe() {
        NoteEquipe note1 = new NoteEquipe();
        NoteEquipe note2 = new NoteEquipe();
        when(noteEquipeService.getNotesByEquipeId(anyInt(), anyInt(), anyInt())).thenReturn(Arrays.asList(note1, note2));

        ResponseEntity<List<NoteEquipe>> result = noteEquipeController.getNotesEquipe(1, 1, 1);

        assertEquals(2, result.getBody().size());
    }

    @Test
    void testGetNotesSprint() {
        NoteEquipe note1 = new NoteEquipe();
        NoteEquipe note2 = new NoteEquipe();
        when(noteEquipeService.getNotesSprint(anyInt(), anyInt())).thenReturn(Arrays.asList(note1, note2));

        List<NoteEquipe> result = noteEquipeController.getNotesSprint(1, 1);

        assertEquals(2, result.size());
    }

    @Test
    void testGetNoteType() {
        NoteEquipe note = new NoteEquipe();
        when(noteEquipeService.getNoteType(anyInt(), anyInt(), any())).thenReturn(note);

        NoteEquipe result = noteEquipeController.getNoteType(1, 1, TypeEchelle.PRESENTATION);

        assertEquals(note, result);
    }

    @Test
    void testGetMoyenneSprint() {
        when(noteEquipeService.getMoyenneSprint(anyInt(), anyInt())).thenReturn(5.0);

        double result = noteEquipeController.getMoyenneSprint(1, 1);

        assertEquals(5.0, result);
    }

    @Test
    void testNoterEquipe() {
        NoteEquipe note = new NoteEquipe();
        when(noteEquipeService.saveNoteEquipe(any(NoteEquipe.class))).thenReturn(note);

        ResponseEntity<NoteEquipe> result = noteEquipeController.noterEquipe(note);

        assertEquals(note, result.getBody());
    }

    @Test
    void testNoterEquipeMultiple() {
        NoteEquipe note1 = new NoteEquipe();
        note1.setTypeNoteEquipe(TypeNoteEquipe.SU_PR);
        NoteEquipe note2 = new NoteEquipe();
        note2.setTypeNoteEquipe(TypeNoteEquipe.OT_PR);
        when(noteEquipeService.saveNoteEquipe(any(NoteEquipe.class))).thenReturn(note1).thenReturn(note2);

        ResponseEntity<List<NoteEquipe>> result = noteEquipeController.noterEquipeMultiple(Arrays.asList(note1, note2));

        assertEquals(2, result.getBody().size());
    }

    @Test
    void noterEquipeReturnsBadRequestWhenNoteIsNull() {
        NoteEquipe note = null;
        when(noteEquipeService.saveNoteEquipe(any(NoteEquipe.class))).thenThrow(new IllegalArgumentException());

        ResponseEntity<NoteEquipe> result = noteEquipeController.noterEquipe(note);

        assertEquals(201, result.getStatusCodeValue());
    }

    @Test
    void getNotesByTypeAndNameReturnsBadRequestWhenUserNotFound() {
        when(utilisateurService.findByNomAndPrenom(anyString(), anyString())).thenThrow(new IllegalArgumentException());

        ResponseEntity<List<NoteEquipe>> result = noteEquipeController.getNotesByTypeAndName("type", 1, "nom", "prenom");

        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    void getMoyennesEquipesReturnsEmptyListWhenNoSprints() {
        when(sprintService.getAllSprints()).thenReturn(Collections.emptyList());

        ResponseEntity<List<MoyenneEquipeResponse>> result = noteEquipeController.getMoyennesEquipes();

        assertTrue(result.getBody().isEmpty());
    }


    @Test
    void testGetNotesByTypeAndName() {
        // Prepare test data
        String type = "testType";
        Integer sprint = 1;
        Integer eleve = 1;
        Utilisateur utilisateur = new Utilisateur();
        Equipe equipe = new Equipe();
        utilisateur.setEquipe(equipe);
        List<NoteEquipe> expectedNotes = new ArrayList<>();

        // Mock the behavior of utilisateurService and noteEquipeService
        when(utilisateurService.getUtilisateurById(eleve)).thenReturn(utilisateur);
        when(noteEquipeService.getNotesByTypeAndTeam(type, sprint, equipe.getId())).thenReturn(expectedNotes);

        // Call the method under test
        ResponseEntity<List<NoteEquipe>> result = noteEquipeController.getNotesByTypeAndName(type, sprint, eleve);

        // Verify the results
        assertEquals(expectedNotes, result.getBody());

        // Verify the interactions with the mock
        verify(utilisateurService, times(1)).getUtilisateurById(eleve);
        verify(noteEquipeService, times(1)).getNotesByTypeAndTeam(type, sprint, equipe.getId());
    }


    @Test
    void testNoterEquipe_ExistingNote() {
        // Prepare test data
        NoteEquipe noteEquipe = new NoteEquipe();
        noteEquipe.setCommentaire("Test commentaire");
        noteEquipe.setNote(15.5);
        noteEquipe.setTypeNoteEquipe(TypeNoteEquipe.SU_PR);

        List<NoteEquipe> noteEquipeList = new ArrayList<>();
        noteEquipeList.add(noteEquipe);

        // Mock the behavior of noteEquipeService
        when(noteEquipeService.findNoteEquipeByCriteria(noteEquipe)).thenReturn(Optional.of(noteEquipe));
        when(noteEquipeService.saveNoteEquipe(noteEquipe)).thenReturn(noteEquipe);

        // Call the method under test
        ResponseEntity<List<NoteEquipe>> result = noteEquipeController.noterEquipeMultiple(noteEquipeList);

        // Verify the results
        assertEquals(noteEquipe, result.getBody().get(0));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the interactions with the mock
        verify(noteEquipeService, times(1)).saveNoteEquipe(noteEquipe);
    }

    @Test
    void testNoterEquipe_NonExistingNote() {
        // Prepare test data
        NoteEquipe noteEquipe = new NoteEquipe();
        noteEquipe.setCommentaire("Test commentaire");
        noteEquipe.setNote(15.5);
        noteEquipe.setTypeNoteEquipe(TypeNoteEquipe.SU_PR);

        List<NoteEquipe> noteEquipeList = new ArrayList<>();
        noteEquipeList.add(noteEquipe);

        // Mock the behavior of noteEquipeService
        when(noteEquipeService.findNoteEquipeByCriteria(noteEquipe)).thenReturn(Optional.empty());
        when(noteEquipeService.saveNoteEquipe(noteEquipe)).thenReturn(noteEquipe);

        // Call the method under test
        ResponseEntity<List<NoteEquipe>> result = noteEquipeController.noterEquipeMultiple(noteEquipeList);

        // Verify the results
        assertEquals(noteEquipe, result.getBody().get(0));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the interactions with the mock
        verify(noteEquipeService, times(1)).saveNoteEquipe(noteEquipe);
    }


    @Test
    void testGetNotesByTypeAndNameWithNomAndPrenom() {
        // Prepare test data
        String type = "testType";
        Integer sprint = 1;
        String nom = "testNom";
        String prenom = "testPrenom";
        Utilisateur utilisateur = new Utilisateur();
        Equipe equipe = new Equipe();
        utilisateur.setEquipe(equipe);
        List<NoteEquipe> expectedNotes = new ArrayList<>();

        // Mock the behavior of utilisateurService and noteEquipeService
        when(utilisateurService.findByNomAndPrenom(nom, prenom)).thenReturn(utilisateur);
        when(noteEquipeService.getNotesByTypeAndTeam(type, sprint, equipe.getId())).thenReturn(expectedNotes);

        // Call the method under test
        ResponseEntity<List<NoteEquipe>> result = noteEquipeController.getNotesByTypeAndName(type, sprint, nom, prenom);

        // Verify the results
        assertEquals(expectedNotes, result.getBody());

        // Verify the interactions with the mock
        verify(utilisateurService, times(1)).findByNomAndPrenom(nom, prenom);
        verify(noteEquipeService, times(1)).getNotesByTypeAndTeam(type, sprint, equipe.getId());
    }


    @Test
    void testGetNotesByTypeAndName_IllegalArgumentException() {
        // Prepare test data
        String type = "testType";
        Integer sprint = 1;
        Integer eleve = 1;

        // Mock the behavior of utilisateurService to throw an IllegalArgumentException
        when(utilisateurService.getUtilisateurById(eleve)).thenThrow(new IllegalArgumentException());

        // Call the method under test
        ResponseEntity<List<NoteEquipe>> result = noteEquipeController.getNotesByTypeAndName(type, sprint, eleve);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        // Verify the interactions with the mock
        verify(utilisateurService, times(1)).getUtilisateurById(eleve);
    }


    @Test
    void testGetMoyennesEquipes() {
        // Prepare test data
        Sprint sprint = new Sprint();
        sprint.setId(1);
        sprint.setStartDate(LocalDate.now());
        List<Sprint> sprints = Arrays.asList(sprint);

        Equipe equipe = new Equipe();
        equipe.setId(1);
        List<Equipe> equipes = Arrays.asList(equipe);

        Double expectedMoyenne = 15.0;
        MoyenneEquipeResponse expectedResponse = MoyenneEquipeResponse.builder()
                .idEquipe(equipe.getId())
                .idSprint(sprint.getId())
                .moyenne(expectedMoyenne)
                .date(sprint.getStartDate())
                .build();
        List<MoyenneEquipeResponse> expectedResponses = Arrays.asList(expectedResponse);

        // Mock the behavior of sprintService, equipeService and noteEquipeService
        when(sprintService.getAllSprints()).thenReturn(sprints);
        when(equipeService.getAllEquipes()).thenReturn(equipes);
        when(noteEquipeService.getMoyenneSprintOptional(equipe.getId(), sprint.getId())).thenReturn(Optional.of(expectedMoyenne));

        // Call the method under test
        ResponseEntity<List<MoyenneEquipeResponse>> result = noteEquipeController.getMoyennesEquipes();

        // Verify the results
        assertEquals(expectedResponses.size(), result.getBody().size());
        assertEquals(expectedResponse, result.getBody().get(0));

        // Verify the interactions with the mock
        verify(sprintService, times(1)).getAllSprints();
        verify(equipeService, times(1)).getAllEquipes();
        verify(noteEquipeService, times(1)).getMoyenneSprintOptional(equipe.getId(), sprint.getId());
    }
}
