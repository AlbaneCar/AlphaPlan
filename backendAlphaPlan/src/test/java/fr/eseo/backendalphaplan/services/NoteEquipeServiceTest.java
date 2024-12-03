package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEquipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import fr.eseo.backendalphaplan.model.NoteEquipe;
import fr.eseo.backendalphaplan.repository.NoteEquipeRepository;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;

class NoteEquipeServiceTest {

    @InjectMocks
    private NoteEquipeService noteEquipeService;

    @Mock
    private NoteEquipeRepository noteEquipeRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetNotesSprint() {
        NoteEquipe note1 = new NoteEquipe();
        NoteEquipe note2 = new NoteEquipe();
        when(noteEquipeRepository.getNotesSprint(anyInt(), anyInt())).thenReturn(Arrays.asList(note1, note2));

        List<NoteEquipe> result = noteEquipeService.getNotesSprint(1, 1);

        assertEquals(2, result.size());
    }

    @Test
    void testGetNoteType() {
        NoteEquipe note = new NoteEquipe();
        when(noteEquipeRepository.getNoteType(anyInt(), anyInt(), any())).thenReturn(note);

        NoteEquipe result = noteEquipeService.getNoteType(1, 1, TypeEchelle.GESTION);

        assertEquals(note, result);
    }


    @Test
    void testGetMoyenneSprint() {
        when(noteEquipeRepository.getMoyenneSprint(anyInt(), anyInt())).thenReturn(5.0);

        double result = noteEquipeService.getMoyenneSprint(1, 1);

        assertEquals(5.0, result);
    }

    @Test
    void testGetNotesByEquipeId() {
        NoteEquipe note1 = new NoteEquipe();
        NoteEquipe note2 = new NoteEquipe();
        when(noteEquipeRepository.findNoteEquipeByEquipeIdAndSprintId(anyInt(), anyInt(),anyInt())).thenReturn(Arrays.asList(note1, note2));

        List<NoteEquipe> result = noteEquipeService.getNotesByEquipeId(1, 1, 1);

        assertEquals(2, result.size());
    }

  @Test
    void saveMultipleNoteEquipe_savesAndReturnsEmptyListWhenInputIsEmpty() {
        var result = noteEquipeService.saveMultipleNoteEquipe(Collections.emptyList());

        assertEquals(0, result.size());
        verify(noteEquipeRepository, times(0)).save(any(NoteEquipe.class));
    }

    @Test
    void saveMultipleNoteEquipe_savesAndReturnsSingleNoteEquipeWhenInputHasOneElement() {
        NoteEquipe note1 = new NoteEquipe();
        Equipe equipe = new Equipe();
        equipe.setId(1);
        note1.setEquipe(equipe);
        Sprint sprint = new Sprint();
        sprint.setId(1);
        note1.setSprint(sprint);
        when(noteEquipeRepository.save(any(NoteEquipe.class))).thenReturn(note1);

        var result = noteEquipeService.saveMultipleNoteEquipe(Collections.singletonList(note1));

        assertEquals(1, result.size());
        verify(noteEquipeRepository, times(2)).save(any(NoteEquipe.class));
    }

    @Test
    void saveMultipleNoteEquipe_throwsExceptionWhenInputIsNull() {
        assertThrows(NullPointerException.class, () -> noteEquipeService.saveMultipleNoteEquipe(null));
    }

    @Test
    void findNoteEquipeByCriteria_returnsOptionalNoteEquipe() {
        NoteEquipe noteEquipe = new NoteEquipe();

        Equipe equipe = new Equipe();
        equipe.setId(1);
        noteEquipe.setEquipe(equipe);

        Utilisateur evaluateur = new Utilisateur();
        evaluateur.setId(1);
        noteEquipe.setEvaluateur(evaluateur);

        Sprint sprint = new Sprint();
        sprint.setId(1);
        noteEquipe.setSprint(sprint);

        when(noteEquipeRepository.findNoteEquipeByEquipeIdAndEvaluateurIdAndSprintId(
                noteEquipe.getEquipe().getId(),
                noteEquipe.getEvaluateur().getId(),
                noteEquipe.getSprint().getId(),
                noteEquipe.getTypeNoteEquipe()
        )).thenReturn(Optional.of(noteEquipe));

        Optional<NoteEquipe> result = noteEquipeService.findNoteEquipeByCriteria(noteEquipe);

        verify(noteEquipeRepository, times(1)).findNoteEquipeByEquipeIdAndEvaluateurIdAndSprintId(
                noteEquipe.getEquipe().getId(),
                noteEquipe.getEvaluateur().getId(),
                noteEquipe.getSprint().getId(),
                noteEquipe.getTypeNoteEquipe()
        );
        assertEquals(Optional.of(noteEquipe), result);
    }

    @Test
    void testFindType() {
        TypeNoteEquipe result = noteEquipeService.findType("PRMA");

        assertEquals(TypeNoteEquipe.PR_MA, result);
    }

    @Test
    void testSaveNoteEquipe() {
        NoteEquipe note = new NoteEquipe();
        Equipe equipe = new Equipe();
        equipe.setId(1);
        note.setEquipe(equipe);
        Sprint sprint = new Sprint();
        sprint.setId(1);
        note.setSprint(sprint);

        when(noteEquipeRepository.save(any(NoteEquipe.class))).thenReturn(note);

        NoteEquipe result = noteEquipeService.saveNoteEquipe(note);

        verify(noteEquipeRepository, times(1)).save(note);
        assertEquals(note, result);
    }

    @Test
    void testCalculTeWo() {
        NoteEquipe noteEquipe = new NoteEquipe();
        Equipe equipe = new Equipe();
        equipe.setId(1);
        noteEquipe.setEquipe(equipe);
        Sprint sprint = new Sprint();
        sprint.setId(1);
        noteEquipe.setSprint(sprint);

        when(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(any(), anyInt(), anyInt())).thenReturn(5.0);
        when(noteEquipeRepository.findNotesByEquipeAndSprintAndType(any(), anyInt(), anyInt())).thenReturn(Arrays.asList(noteEquipe));

        noteEquipeService.calculTeWo(noteEquipe);

        verify(noteEquipeRepository, times(1)).deleteAll(anyList());
        verify(noteEquipeRepository, times(1)).save(any(NoteEquipe.class));
    }
}
