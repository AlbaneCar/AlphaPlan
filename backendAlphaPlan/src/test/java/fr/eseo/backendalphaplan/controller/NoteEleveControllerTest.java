package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.NoteEleve;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import fr.eseo.backendalphaplan.services.NoteEleveService;
import fr.eseo.backendalphaplan.services.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NoteEleveControllerTest {

    private MockMvc mockMvc;


    @Mock
    private NoteEleveService noteEleveService;

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private NoteEleveController noteEleveController;

    @Mock
    private UtilisateurController utilisateurController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(noteEleveController).build();
    }

    @Test
    void testModifierNoteEleve() {
        // Création des données de test
        int id = 1;
        float nouvelleNote = 15.5f;

        // Création du requestBody pour la modification de la note
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("note", nouvelleNote);

        // Mock du service pour retourner une note existante
        NoteEleve noteExistante = new NoteEleve();
        when(noteEleveService.getNoteEleve(id)).thenReturn(Optional.of(noteExistante));

        // Appel de la méthode à tester
        ResponseEntity<NoteEleve> response = noteEleveController.modifierNoteEleve(id, requestBody);

        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nouvelleNote, noteExistante.getNote());
        verify(noteEleveService, times(1)).saveNoteEleve(noteExistante);
    }

    @Test
    void testModifierNoteEleve_NoteInexistante() {
        // Création des données de test
        int id = 1;

        // Mock du service pour retourner une note inexistante
        when(noteEleveService.getNoteEleve(id)).thenReturn(Optional.empty());

        // Appel de la méthode à tester
        ResponseEntity<NoteEleve> response = noteEleveController.modifierNoteEleve(id, new HashMap<>());

        // Vérification
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(noteEleveService, never()).saveNoteEleve(any());
    }

    @Test
    void testGetNotesSprint() {
        // Prepare test data
        Integer id = 1;
        int sprint = 1;
        List<NoteEleve> expectedNotes = new ArrayList<>();

        // Mock the behavior of noteEleveService
        when(noteEleveService.getNotesSprint(id, sprint)).thenReturn(expectedNotes);

        // Call the method under test
        List<NoteEleve> result = noteEleveController.getNotesSprint(id, sprint);

        // Verify the results
        assertEquals(expectedNotes, result);

        // Verify the interactions with the mock
        verify(noteEleveService, times(1)).getNotesSprint(id, sprint);
    }

    @Test
    void testGetNoteByType() {
        // Prepare test data
        Integer id = 1;
        TypeNoteEleve type = TypeNoteEleve.IN_SP;
        int sprint = 1;
        NoteEleve expectedNote = new NoteEleve();

        // Mock the behavior of noteEleveService
        when(noteEleveService.getNoteByType(id, type, sprint)).thenReturn(expectedNote);

        // Call the method under test
        NoteEleve result = noteEleveController.getNoteByType(id, type, sprint);

        // Verify the results
        assertEquals(expectedNote, result);

        // Verify the interactions with the mock
        verify(noteEleveService, times(1)).getNoteByType(id, type, sprint);
    }

    @Test
    void testGetMoyenneSprint() {
        // Prepare test data
        Integer id = 1;
        int sprint = 1;
        double expectedMoyenne = 15.0;

        // Mock the behavior of noteEleveService
        when(noteEleveService.getMoyenneSprint(id, sprint)).thenReturn(expectedMoyenne);

        // Call the method under test
        double result = noteEleveController.getMoyenneSprint(id, sprint);

        // Verify the results
        assertEquals(expectedMoyenne, result);

        // Verify the interactions with the mock
        verify(noteEleveService, times(1)).getMoyenneSprint(id, sprint);
    }

    @Test
    void testNoterEleve() {
        // Prepare test data
        NoteEleve noteEleve = new NoteEleve();
        noteEleve.setTypeNoteEleve(TypeNoteEleve.SS_PR); // Set a value for TypeNoteEleve
        List<NoteEleve> noteEleveList = new ArrayList<>();
        noteEleveList.add(noteEleve);

        // Mock the behavior of noteEleveService
        when(noteEleveService.saveNoteEleve(noteEleve)).thenReturn(noteEleve);

        // Call the method under test
        ResponseEntity<List<NoteEleve>> result = noteEleveController.noterEleve(noteEleveList);

        // Verify the results
        assertEquals(noteEleve, result.getBody().get(0));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the interactions with the mock
        verify(noteEleveService, times(1)).saveNoteEleve(noteEleve);
    }

    @Test
    void testGetNotesSprintAfterBM() {
        // Prepare test data
        Integer id = 1;
        int sprint = 1;
        List<Map<String, Object>> expectedNotes = new ArrayList<>();

        // Mock the behavior of noteEleveService
        when(noteEleveService.getNotesSprintAfterBM(id, sprint)).thenReturn(expectedNotes);

        // Call the method under test
        List<Map<String, Object>> result = noteEleveController.getNotesSprintAfterBM(id, sprint);

        // Verify the results
        assertEquals(expectedNotes, result);

        // Verify the interactions with the mock
        verify(noteEleveService, times(1)).getNotesSprintAfterBM(id, sprint);
    }

    @Test
    void testGetMoyenneSprintAfterBM() {
        // Prepare test data
        Integer id = 1;
        int sprint = 1;
        double expectedMoyenne = 15.0;

        // Mock the behavior of noteEleveService
        when(noteEleveService.getMoyenneSprintAfterBM(id, sprint)).thenReturn(expectedMoyenne);

        // Call the method under test
        double result = noteEleveController.getMoyenneSprintAfterBM(id, sprint);

        // Verify the results
        assertEquals(expectedMoyenne, result);

        // Verify the interactions with the mock
        verify(noteEleveService, times(1)).getMoyenneSprintAfterBM(id, sprint);
    }

    @Test
    void testGetBonusMalus() {
        // Prepare test data
        Integer id = 1;
        Integer idSprint = 1;
        List<NoteEleve> expectedNotes = new ArrayList<>();

        // Mock the behavior of noteEleveService
        when(noteEleveService.getBonusMalusByUserAndSprint(id, idSprint)).thenReturn(expectedNotes);

        // Call the method under test
        List<NoteEleve> result = noteEleveController.getBonusMalus(id, idSprint);

        // Verify the results
        assertEquals(expectedNotes, result);

        // Verify the interactions with the mock
        verify(noteEleveService, times(1)).getBonusMalusByUserAndSprint(id, idSprint);
    }

    @Test
    void testGetBonusMalusByEquipe() {
        // Prepare test data
        Integer idEquipe = 1;
        Integer idSprint = 1;
        List<Utilisateur> eleves = new ArrayList<>();
        List<NoteEleve> expectedNotes = new ArrayList<>();

        // Mock the behavior of utilisateurController and noteEleveService
        when(utilisateurController.getUsersByEquipeId(idEquipe)).thenReturn(eleves);
        for (Utilisateur eleve : eleves) {
            when(noteEleveService.getBonusMalusByUserAndSprint(eleve.getId(), idSprint)).thenReturn(expectedNotes);
        }

        // Call the method under test
        ResponseEntity<List<NoteEleve>> result = noteEleveController.getBonusMalusByEquipe(idEquipe, idSprint);

        // Verify the results
        assertEquals(expectedNotes, result.getBody());

        // Verify the interactions with the mock
        for (Utilisateur eleve : eleves) {
            verify(noteEleveService, times(1)).getBonusMalusByUserAndSprint(eleve.getId(), idSprint);
        }
    }
    @Test
    void getNotesByTypeAndNameReturnsExpectedNotes() {
        String type = "TYPE";
        Integer sprint = 1;
        String nom = "John";
        String prenom = "Doe";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1);
        List<NoteEleve> expectedNotes = new ArrayList<>();

        when(utilisateurService.findByNomAndPrenom(nom, prenom)).thenReturn(utilisateur);
        when(noteEleveService.getNotesByTypeAndUser(type, sprint, utilisateur.getId())).thenReturn(expectedNotes);

        ResponseEntity<List<NoteEleve>> result = noteEleveController.getNotesByTypeAndName(type, sprint, nom, prenom);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedNotes, result.getBody());
    }

    @Test
    void getNotesByTypeAndNameReturnsBadRequestWhenInvalidData() {
        String type = "TYPE";
        Integer sprint = 1;
        String nom = "John";
        String prenom = "Doe";

        when(utilisateurService.findByNomAndPrenom(nom, prenom)).thenThrow(IllegalArgumentException.class);

        ResponseEntity<List<NoteEleve>> result = noteEleveController.getNotesByTypeAndName(type, sprint, nom, prenom);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void deleteNoteEleveDeletesNote() {
        Integer id = 1;

        doNothing().when(noteEleveService).deleteNoteEleve(id);

        ResponseEntity<Void> result = noteEleveController.deleteNoteEleve(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(noteEleveService, times(1)).deleteNoteEleve(id);
    }

    @Test
    void getNotesEleveReturnsExpectedNotes() {
        Integer idEleve = 1;
        Integer idSprint = 1;
        Integer idEvaluateur = 1;
        List<NoteEleve> expectedNotes = new ArrayList<>();

        when(noteEleveService.getNotesByEleveId(idEleve, idSprint, idEvaluateur)).thenReturn(expectedNotes);

        ResponseEntity<List<NoteEleve>> result = noteEleveController.getNotesEleve(idEleve, idSprint, idEvaluateur);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedNotes, result.getBody());
    }

    @Test
    void testGetNotesByTypeAndName() throws Exception {
        String type = "TYPE";
        Integer sprint = 1;
        Integer eleveId = 1;
        List<NoteEleve> notes = Arrays.asList(new NoteEleve(), new NoteEleve());

        when(noteEleveService.getNotesByTypeAndUser(type, sprint, eleveId)).thenReturn(notes);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/notesEleve/{type}/{sprint}/{eleveId}", type, sprint, eleveId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]")); // assuming NoteEleve objects are empty
    }

    @Test
    void testGetNotesByTypeAndNameReturnsBadRequestWhenInvalidData() throws Exception {
        String type = "TYPE";
        Integer sprint = 1;
        Integer eleveId = 1;

        when(noteEleveService.getNotesByTypeAndUser(type, sprint, eleveId)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/notesEleve/{type}/{sprint}/{eleveId}", type, sprint, eleveId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testModifierNoteEleveWithCommentaire() {
        // Création des données de test
        int id = 1;
        String commentaire = "Test commentaire";

        // Création du requestBody pour la modification de la note
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("commentaire", commentaire);

        // Mock du service pour retourner une note existante
        NoteEleve noteExistante = new NoteEleve();
        when(noteEleveService.getNoteEleve(id)).thenReturn(Optional.of(noteExistante));

        // Appel de la méthode à tester
        ResponseEntity<NoteEleve> response = noteEleveController.modifierNoteEleve(id, requestBody);

        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commentaire, noteExistante.getCommentaire());
        verify(noteEleveService, times(1)).saveNoteEleve(noteExistante);
    }


    @Test
    void testNoterEleve_ExistingNote() {
        // Prepare test data
        NoteEleve noteEleve = new NoteEleve();
        noteEleve.setCommentaire("Test commentaire");
        noteEleve.setNote(15.3);
        noteEleve.setTypeNoteEleve(TypeNoteEleve.SS_PR);

        List<NoteEleve> noteEleveList = new ArrayList<>();
        noteEleveList.add(noteEleve);

        // Mock the behavior of noteEleveService
        when(noteEleveService.findNoteEleveByCriteria(noteEleve)).thenReturn(Optional.of(noteEleve));
        when(noteEleveService.saveNoteEleve(noteEleve)).thenReturn(noteEleve);

        // Call the method under test
        ResponseEntity<List<NoteEleve>> result = noteEleveController.noterEleve(noteEleveList);

        // Verify the results
        assertEquals(noteEleve, result.getBody().get(0));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the interactions with the mock
        verify(noteEleveService, times(1)).saveNoteEleve(noteEleve);
    }

    @Test
    void testNoterEleve_NonExistingNote() {
        // Prepare test data
        NoteEleve noteEleve = new NoteEleve();
        noteEleve.setCommentaire("Test commentaire");
        noteEleve.setNote(15.5);
        noteEleve.setTypeNoteEleve(TypeNoteEleve.SS_PR);

        List<NoteEleve> noteEleveList = new ArrayList<>();
        noteEleveList.add(noteEleve);

        // Mock the behavior of noteEleveService
        when(noteEleveService.findNoteEleveByCriteria(noteEleve)).thenReturn(Optional.empty());
        when(noteEleveService.saveNoteEleve(noteEleve)).thenReturn(noteEleve);

        // Call the method under test
        ResponseEntity<List<NoteEleve>> result = noteEleveController.noterEleve(noteEleveList);

        // Verify the results
        assertEquals(noteEleve, result.getBody().get(0));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the interactions with the mock
        verify(noteEleveService, times(1)).saveNoteEleve(noteEleve);
    }




}