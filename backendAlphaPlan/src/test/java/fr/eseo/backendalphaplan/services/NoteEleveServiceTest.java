package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.NoteEleve;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEquipe;
import fr.eseo.backendalphaplan.repository.NoteEleveRepository;
import fr.eseo.backendalphaplan.repository.NoteEquipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NoteEleveServiceTest {

    @InjectMocks
    private NoteEleveService noteEleveService;

    @Mock
    private NoteEleveRepository noteEleveRepository;

    @Mock
    private NoteEquipeRepository noteEquipeRepository;


    private NoteEleve noteEleve;
    private Sprint sprint;
    private Utilisateur eleve;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        sprint = new Sprint();
        sprint.setId(1);

        eleve = new Utilisateur();
        eleve.setId(1);

        noteEleve = new NoteEleve();
        noteEleve.setSprint(sprint);
        noteEleve.setEleve(eleve);
    }

    @Test
    void testGetNotesSprint() {
        NoteEleve note = new NoteEleve();
        when(noteEleveRepository.getNotesSprint(anyInt(), anyInt())).thenReturn(Collections.singletonList(note));

        var result = noteEleveService.getNotesSprint(1, 1);

        assertEquals(1, result.size());
    }

    @Test
    void testGetNoteByType() {
        NoteEleve note = new NoteEleve();
        when(noteEleveRepository.getNoteByType(anyInt(), any(TypeNoteEleve.class), anyInt())).thenReturn(note);

        var result = noteEleveService.getNoteByType(1, TypeNoteEleve.IN_SP, 1);

        assertEquals(note, result);
    }

    @Test
    void testGetMoyenneSprint() {
        when(noteEleveRepository.getMoyenneSprint(anyInt(), anyInt())).thenReturn(10.0);

        double result = noteEleveService.getMoyenneSprint(1, 1);

        assertEquals(10.0, result);
    }

    @Test
    void testSaveNoteEleve() {
        NoteEleve note = new NoteEleve();
        when(noteEleveRepository.save(any(NoteEleve.class))).thenReturn(note);

        var result = noteEleveService.saveNoteEleve(note);

        assertEquals(note, result);
    }

    @Test
    void testGetNotesSprintAfterBM() {
        when(noteEleveRepository.getNotesSprintAfterBM(anyInt(), anyInt())).thenReturn(Collections.singletonList(Collections.emptyMap()));

        var result = noteEleveService.getNotesSprintAfterBM(1, 1);

        assertEquals(1, result.size());
    }

    @Test
    void testGetMoyenneSprintAfterBM() {
        when(noteEleveRepository.getMoyenneSprintAfterBM(anyInt(), anyInt())).thenReturn(10.0);

        double result = noteEleveService.getMoyenneSprintAfterBM(1, 1);

        assertEquals(10.0, result);
    }

    @Test
    void testGetBonusMalusByUserAndSprint() {
        NoteEleve note = new NoteEleve();
        when(noteEleveRepository.findTe_BMbyUtilisateurAndSprint(anyInt(), anyInt())).thenReturn(Collections.singletonList(note));

        var result = noteEleveService.getBonusMalusByUserAndSprint(1, 1);

        assertEquals(1, result.size());
    }

    @Test
    void testGetNoteEleve() {
        NoteEleve note = new NoteEleve();
        when(noteEleveRepository.findById(anyInt())).thenReturn(Optional.of(note));

        var result = noteEleveService.getNoteEleve(1);

        assertEquals(note, result.get());
    }

    @Test
    void testGetNotesByTypeAndUser() {
        NoteEleve note = new NoteEleve();
        when(noteEleveRepository.findNoteEleveByTypeAndUser(any(), anyInt(), anyInt())).thenReturn(List.of(note));

        List<NoteEleve> result = noteEleveService.getNotesByTypeAndUser("SSPR", 1, 1);

        assertEquals(1, result.size());
        assertEquals(note, result.get(0));
    }

    @Test
    void testDeleteNoteEleve() {
        doNothing().when(noteEleveRepository).deleteById(anyInt());
        noteEleveService.deleteNoteEleve(1);

        verify(noteEleveRepository, times(1)).deleteById(1);
    }
    @Test
    void testFindNoteEleveByCriteria() {
        NoteEleve note = new NoteEleve();
        Utilisateur eleve = new Utilisateur();
        Utilisateur evaluateur = new Utilisateur();
        Sprint sprint = new Sprint();
        eleve.setId(1);
        evaluateur.setId(2);
        sprint.setId(3);
        note.setEleve(eleve);
        note.setEvaluateur(evaluateur);
        note.setSprint(sprint);

        when(noteEleveRepository.findNoteEleveByEleveIdAndEvaluateurIdAndSprintId(anyInt(), anyInt(), anyInt(), any())).thenReturn(Optional.of(note));

        Optional<NoteEleve> result = noteEleveService.findNoteEleveByCriteria(note);

        Assertions.assertTrue(result.isPresent());
        assertEquals(note, result.get());
    }
    @Test
    void shouldDeleteNoteEleveWhenGivenValidId() {
        doNothing().when(noteEleveRepository).deleteById(anyInt());
        noteEleveService.deleteNoteEleve(1);
        verify(noteEleveRepository, times(1)).deleteById(1);
    }

    @Test
    void shouldReturnNoteEleveWhenFindNoteEleveByCriteriaIsCalledWithValidCriteria() {
        NoteEleve note = new NoteEleve();
        Utilisateur eleve = new Utilisateur();
        Utilisateur evaluateur = new Utilisateur();
        Sprint sprint = new Sprint();
        eleve.setId(1);
        evaluateur.setId(2);
        sprint.setId(3);
        note.setEleve(eleve);
        note.setEvaluateur(evaluateur);
        note.setSprint(sprint);
        note.setTypeNoteEleve(TypeNoteEleve.IN_SP);

        when(noteEleveRepository.findNoteEleveByEleveIdAndEvaluateurIdAndSprintId(anyInt(), anyInt(), anyInt(), any())).thenReturn(Optional.of(note));

        Optional<NoteEleve> result = noteEleveService.findNoteEleveByCriteria(note);

        Assertions.assertTrue(result.isPresent());
        assertEquals(note, result.get());
    }

    @Test
    void shouldReturnNotesWhenGetNotesByEleveIdIsCalledWithValidIds() {
        NoteEleve note = new NoteEleve();
        when(noteEleveRepository.getNotesEleves(anyInt(), anyInt(), anyInt())).thenReturn(Collections.singletonList(note));

        List<NoteEleve> result = noteEleveService.getNotesByEleveId(1, 1, 1);

        assertEquals(1, result.size());
        assertEquals(note, result.get(0));
    }



    @Test
    //antoine
     void testCalculIgSpWithExistingNotes() {
        when(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IN_SP, eleve.getId(), sprint.getId())).thenReturn(15.0);
        when(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IN_PR, eleve.getId(), sprint.getId())).thenReturn(10.0);
        when(noteEleveRepository.findNoteEleveByTypeAndUser(TypeNoteEleve.IG_SP, sprint.getId(), eleve.getId()))
                .thenReturn(List.of(new NoteEleve()));


        noteEleveService.calculIgSp(noteEleve);

        verify(noteEleveRepository).deleteAll(anyList());
        verify(noteEleveRepository).save(any(NoteEleve.class));


        ArgumentCaptor<NoteEleve> noteEleveCaptor = ArgumentCaptor.forClass(NoteEleve.class);
        verify(noteEleveRepository).save(noteEleveCaptor.capture());
        NoteEleve savedNote = noteEleveCaptor.getValue();

        assertEquals(eleve, savedNote.getEleve());
        assertEquals(sprint, savedNote.getSprint());
        assertEquals(TypeNoteEleve.IG_SP, savedNote.getTypeNoteEleve());
        assertEquals(0.7 * 15.0 + 0.3 * 10.0, savedNote.getNote(), 0.01);
    }


    @Test
    //antoine
    void testCalculIgSpWithoutExistingIgSpNote() {
        when(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IN_SP, eleve.getId(), sprint.getId())).thenReturn(15.0);
        when(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IN_PR, eleve.getId(), sprint.getId())).thenReturn(10.0);
        when(noteEleveRepository.findNoteEleveByTypeAndUser(TypeNoteEleve.IG_SP, sprint.getId(), eleve.getId()))
                .thenReturn(Collections.emptyList());


        noteEleveService.calculIgSp(noteEleve);


        //verify(noteEleveRepository, never()).deleteAll(anyList());
        verify(noteEleveRepository).save(any(NoteEleve.class));


        ArgumentCaptor<NoteEleve> noteEleveCaptor = ArgumentCaptor.forClass(NoteEleve.class);
        verify(noteEleveRepository).save(noteEleveCaptor.capture());
        NoteEleve savedNote = noteEleveCaptor.getValue();

        assertEquals(eleve, savedNote.getEleve());
        assertEquals(sprint, savedNote.getSprint());
        assertEquals(TypeNoteEleve.IG_SP, savedNote.getTypeNoteEleve());
        assertEquals(0.7 * 15.0 + 0.3 * 10.0, savedNote.getNote(), 0.01);
    }

    @Test
    //antoine
    void testCalculIgSpWithNullNotes() {
        when(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IN_SP, eleve.getId(), sprint.getId())).thenReturn(null);
        when(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.IN_PR, eleve.getId(), sprint.getId())).thenReturn(null);


        noteEleveService.calculIgSp(noteEleve);


        verify(noteEleveRepository, never()).deleteAll(anyList());
        verify(noteEleveRepository, never()).save(any(NoteEleve.class));
    }

    @Test
    //antoine
    void testCalculIgSpWithNullSprintOrEleve() {
        noteEleve.setSprint(null);
        noteEleveService.calculIgSp(noteEleve);
        verify(noteEleveRepository, never()).findNoteByTypeAndUtilisateurAndSprint(any(), anyInt(), anyInt());
        verify(noteEleveRepository, never()).save(any(NoteEleve.class));


        noteEleve.setSprint(sprint);
        noteEleve.setEleve(null);
        noteEleveService.calculIgSp(noteEleve);
        verify(noteEleveRepository, never()).findNoteByTypeAndUtilisateurAndSprint(any(), anyInt(), anyInt());
        verify(noteEleveRepository, never()).save(any(NoteEleve.class));
    }


    @Test
    //antoine
    void testCalculInPrWithExistingNotes() {
        when(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.SS_PR, eleve.getId(), sprint.getId())).thenReturn(15.0);
        when(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.OT_PR, eleve.getId(), sprint.getId())).thenReturn(10.0);
        when(noteEleveRepository.findNoteEleveByTypeAndUser(TypeNoteEleve.IN_PR, sprint.getId(), eleve.getId()))
                .thenReturn(List.of(new NoteEleve()));

        noteEleveService.calculInPr(noteEleve);


        verify(noteEleveRepository).deleteAll(anyList());
        verify(noteEleveRepository).save(any(NoteEleve.class));

        ArgumentCaptor<NoteEleve> noteEleveCaptor = ArgumentCaptor.forClass(NoteEleve.class);
        verify(noteEleveRepository).save(noteEleveCaptor.capture());
        NoteEleve savedNote = noteEleveCaptor.getValue();

        assertEquals(eleve, savedNote.getEleve());
        assertEquals(sprint, savedNote.getSprint());
        assertEquals(TypeNoteEleve.IN_PR, savedNote.getTypeNoteEleve());
        assertEquals((2 * 15.0 + 10.0) / 3, savedNote.getNote(), 0.01);
    }



    @Test
    //antoine
    void testCalculInPrWithoutExistingInPrNote() {
        when(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.SS_PR, eleve.getId(), sprint.getId())).thenReturn(15.0);
        when(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.OT_PR, eleve.getId(), sprint.getId())).thenReturn(10.0);
        when(noteEleveRepository.findNoteEleveByTypeAndUser(TypeNoteEleve.IN_PR, sprint.getId(), eleve.getId()))
                .thenReturn(Collections.emptyList());


        noteEleveService.calculInPr(noteEleve);


        //verify(noteEleveRepository, never()).deleteAll(anyList());
        verify(noteEleveRepository).save(any(NoteEleve.class));


        ArgumentCaptor<NoteEleve> noteEleveCaptor = ArgumentCaptor.forClass(NoteEleve.class);
        verify(noteEleveRepository).save(noteEleveCaptor.capture());
        NoteEleve savedNote = noteEleveCaptor.getValue();

        assertEquals(eleve, savedNote.getEleve());
        assertEquals(sprint, savedNote.getSprint());
        assertEquals(TypeNoteEleve.IN_PR, savedNote.getTypeNoteEleve());
        assertEquals((2 * 15.0 + 10.0) / 3, savedNote.getNote(), 0.01);
    }


    @Test
    //antoine
    void testCalculInPrWithNullNotes() {
        when(noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(TypeNoteEleve.SS_PR, eleve.getId(), sprint.getId())).thenReturn(null);
        when(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.OT_PR, eleve.getId(), sprint.getId())).thenReturn(null);

        noteEleveService.calculInPr(noteEleve);


        verify(noteEleveRepository, never()).deleteAll(anyList());
        verify(noteEleveRepository, never()).save(any(NoteEleve.class));
    }


    @Test
    //antoine
    void testCalculInPrWithNullSprintOrEleve() {
        noteEleve.setSprint(null);
        noteEleveService.calculInPr(noteEleve);
        verify(noteEleveRepository, never()).findNoteByTypeAndUtilisateurAndSprint(any(), anyInt(), anyInt());
        verify(noteEquipeRepository, never()).findNoteByTypeAndEquipeAndSprint(any(), anyInt(), anyInt());
        verify(noteEleveRepository, never()).save(any(NoteEleve.class));


        noteEleve.setSprint(sprint);
        noteEleve.setEleve(null);
        noteEleveService.calculInPr(noteEleve);
        verify(noteEleveRepository, never()).findNoteByTypeAndUtilisateurAndSprint(any(), anyInt(), anyInt());
        verify(noteEquipeRepository, never()).findNoteByTypeAndEquipeAndSprint(any(), anyInt(), anyInt());
        verify(noteEleveRepository, never()).save(any(NoteEleve.class));
    }


    @Test
    //antoine
    void testCalculInSpWithExistingNotes() {
        when(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.TE_WO, eleve.getId(), sprint.getId())).thenReturn(10.0);
        when(noteEleveRepository.findTEBMByUtilisateurAndSprint(eleve.getId(), sprint.getId())).thenReturn(5.0);
        when(noteEleveRepository.findSSBMByUtilisateurAndSprint(eleve.getId(), sprint.getId())).thenReturn(3.0);
        when(noteEleveRepository.findNoteEleveByTypeAndUser(TypeNoteEleve.IN_SP, sprint.getId(), eleve.getId()))
                .thenReturn(List.of(new NoteEleve()));


        noteEleveService.calculInSp(noteEleve);


        verify(noteEleveRepository).deleteAll(anyList());
        verify(noteEleveRepository).save(any(NoteEleve.class));


        ArgumentCaptor<NoteEleve> noteEleveCaptor = ArgumentCaptor.forClass(NoteEleve.class);
        verify(noteEleveRepository).save(noteEleveCaptor.capture());
        NoteEleve savedNote = noteEleveCaptor.getValue();

        assertEquals(eleve, savedNote.getEleve());
        assertEquals(sprint, savedNote.getSprint());
        assertEquals(TypeNoteEleve.IN_SP, savedNote.getTypeNoteEleve());
        assertEquals(10.0 + 5.0 + 3.0, savedNote.getNote(), 0.01);
    }


    @Test
    //antoine
    void testCalculInSpWithoutExistingInSpNote() {
        when(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.TE_WO, eleve.getId(), sprint.getId())).thenReturn(10.0);
        when(noteEleveRepository.findTEBMByUtilisateurAndSprint(eleve.getId(), sprint.getId())).thenReturn(5.0);
        when(noteEleveRepository.findSSBMByUtilisateurAndSprint(eleve.getId(), sprint.getId())).thenReturn(3.0);
        when(noteEleveRepository.findNoteEleveByTypeAndUser(TypeNoteEleve.IN_SP, sprint.getId(), eleve.getId()))
                .thenReturn(Collections.emptyList());


        noteEleveService.calculInSp(noteEleve);


        //verify(noteEleveRepository, never()).deleteAll(anyList());
        verify(noteEleveRepository).save(any(NoteEleve.class));


        ArgumentCaptor<NoteEleve> noteEleveCaptor = ArgumentCaptor.forClass(NoteEleve.class);
        verify(noteEleveRepository).save(noteEleveCaptor.capture());
        NoteEleve savedNote = noteEleveCaptor.getValue();

        assertEquals(eleve, savedNote.getEleve());
        assertEquals(sprint, savedNote.getSprint());
        assertEquals(TypeNoteEleve.IN_SP, savedNote.getTypeNoteEleve());
        assertEquals(10.0 + 5.0 + 3.0, savedNote.getNote(), 0.01);
    }

    @Test
    //antoine
    void testCalculInSpWithNullNotes() {
        when(noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(TypeNoteEquipe.TE_WO, eleve.getId(), sprint.getId())).thenReturn(null);
        when(noteEleveRepository.findTEBMByUtilisateurAndSprint(eleve.getId(), sprint.getId())).thenReturn(null);
        when(noteEleveRepository.findSSBMByUtilisateurAndSprint(eleve.getId(), sprint.getId())).thenReturn(null);

        noteEleveService.calculInSp(noteEleve);

        verify(noteEleveRepository, never()).deleteAll(anyList());
        verify(noteEleveRepository, never()).save(any(NoteEleve.class));
    }


    @Test
    //antoine
    void testCalculInSpWithNullSprintOrEleve() {
        noteEleve.setSprint(null);

        noteEleveService.calculInSp(noteEleve);
        verify(noteEquipeRepository, never()).findNoteByTypeAndEquipeAndSprint(any(), anyInt(), anyInt());
        verify(noteEleveRepository, never()).findTEBMByUtilisateurAndSprint(anyInt(), anyInt());
        verify(noteEleveRepository, never()).findSSBMByUtilisateurAndSprint(anyInt(), anyInt());
        verify(noteEleveRepository, never()).save(any(NoteEleve.class));
        
        noteEleve.setSprint(sprint);
        noteEleve.setEleve(null);
        noteEleveService.calculInSp(noteEleve);
        verify(noteEquipeRepository, never()).findNoteByTypeAndEquipeAndSprint(any(), anyInt(), anyInt());
        verify(noteEleveRepository, never()).findTEBMByUtilisateurAndSprint(anyInt(), anyInt());
        verify(noteEleveRepository, never()).findSSBMByUtilisateurAndSprint(anyInt(), anyInt());
        verify(noteEleveRepository, never()).save(any(NoteEleve.class));
    }
}