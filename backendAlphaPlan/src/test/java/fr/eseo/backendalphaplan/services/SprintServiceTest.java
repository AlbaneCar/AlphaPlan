package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.*;
import fr.eseo.backendalphaplan.utils.Note;
import fr.eseo.backendalphaplan.utils.NotesUtilisateur;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import fr.eseo.backendalphaplan.model.Sprint;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



class SprintServiceTest {

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private NoteEleveRepository noteEleveRepository;

    @Mock
    private NoteEquipeRepository noteEquipeRepository;

    @InjectMocks
    private SprintService sprintService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllSprints() {
        // Given
        Sprint sprint1 = new Sprint();
        sprint1.setId(1);
        sprint1.setStartDate(LocalDate.now());
        sprint1.setEndDate(LocalDate.now().plusDays(10));

        Sprint sprint2 = new Sprint();
        sprint2.setId(2);
        sprint2.setStartDate(LocalDate.now());
        sprint2.setEndDate(LocalDate.now().plusDays(20));

        List<Sprint> sprints = new ArrayList<>();
        sprints.add(sprint1);
        sprints.add(sprint2);

        when(sprintRepository.findAll()).thenReturn(sprints);

        // When
        List<Sprint> result = sprintService.getAllSprints();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void testGetSprintById() {
        // Given
        Sprint sprint = new Sprint();
        sprint.setId(1);
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusDays(10));

        when(sprintRepository.findById(1)).thenReturn(Optional.of(sprint));

        // When
        Optional<Sprint> result = sprintService.getSprintById(1);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testSaveSprints() {
        // Given
        Sprint sprint1 = new Sprint();
        sprint1.setId(1);
        sprint1.setStartDate(LocalDate.now());
        sprint1.setEndDate(LocalDate.now().plusDays(10));

        Sprint sprint2 = new Sprint();
        sprint2.setId(2);
        sprint2.setStartDate(LocalDate.now());
        sprint2.setEndDate(LocalDate.now().plusDays(20));

        List<Sprint> sprints = new ArrayList<>();
        sprints.add(sprint1);
        sprints.add(sprint2);

        when(sprintRepository.save(any(Sprint.class))).thenReturn(new Sprint());

        // When
        List<Sprint> result = sprintService.saveSprints(sprints);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteSprint() {
        // When
        sprintService.deleteSprint(1);

        // Then
        verify(sprintRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAllSprints() {
        // When
        sprintService.deleteAllSprints();

        // Then
        verify(sprintRepository, times(1)).deleteAll();
    }

    @Test
    void testGetNoteEleveByEleveAndSprint() {
        // Given
        Integer id = 1;
        Integer sprint = 1;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        utilisateur.setEquipe(new Equipe());

        when(utilisateurRepository.findById(id)).thenReturn(Optional.of(utilisateur));
        when(sprintRepository.findById(sprint)).thenReturn(Optional.of(new Sprint()));

        // When
        List<Note> result = sprintService.getNoteEleveByEleveAndSprint(id, sprint);

        // Then
        assertNotNull(result);
    }

    @Test
    void testGetNoteEquipeByEquipeAndSprint() {
        // Given
        Integer equipeId = 1;
        Integer sprintId = 1;
        Equipe equipe = new Equipe();
        equipe.setId(equipeId);

        when(equipeRepository.findById(equipeId)).thenReturn(Optional.of(equipe));
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(new Sprint()));
        when(equipeRepository.getStudentsByEquipeId(equipeId)).thenReturn(new ArrayList<>());

        // When
        List<NotesUtilisateur> result = sprintService.getNoteEquipeByEquipeAndSprint(equipeId, sprintId);

        // Then
        assertNotNull(result);
    }

}