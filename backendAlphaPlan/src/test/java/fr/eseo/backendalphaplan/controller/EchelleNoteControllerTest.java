package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.EchelleNote;
import fr.eseo.backendalphaplan.model.Echelon;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;
import fr.eseo.backendalphaplan.services.EchelleNoteService;
import fr.eseo.backendalphaplan.services.EchelonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EchelleNoteControllerTest {

    @InjectMocks
    EchelleNoteController echelleNoteController;

    @Mock
    EchelleNoteService echelleNoteService;

    @Mock
    EchelonService echelonService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return all EchelleNotes with their Echelons")
    void getAllEchelleNotes() {
        EchelleNote echelleNote = new EchelleNote();
        echelleNote.setId(1);
        Echelon echelon = new Echelon();
        echelon.setId(1);
        echelon.setEchelleNoteId(echelleNote);
        when(echelleNoteService.getAllEchelleNotes()).thenReturn(Arrays.asList(echelleNote));
        when(echelonService.getEchelonsByEchelleNoteId(echelleNote)).thenReturn(Arrays.asList(echelon));

        List<EchelleNote> result = echelleNoteController.getAllEchelleNotes();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getEchelons().size());
    }



    @Test
    @DisplayName("Should delete EchelleNote and its Echelons")
    void supprimerEchelleNote() {
        EchelleNote echelleNote = new EchelleNote();
        echelleNote.setId(1);
        Echelon echelon = new Echelon();
        echelon.setId(1);
        echelon.setEchelleNoteId(echelleNote);
        when(echelleNoteService.getEchelleNoteById(1)).thenReturn(echelleNote);
        when(echelonService.getEchelonsByEchelleNoteId(echelleNote)).thenReturn(Arrays.asList(echelon));

        echelleNoteController.supprimerEchelleNote(1);

        verify(echelleNoteService, times(1)).supprimerEchelle(1);
        verify(echelonService, times(1)).delete(echelon);
    }

    @Test
    @DisplayName("Should return all TypeEchelles")
    void getAllTypeEchelles() {
        List<TypeEchelle> result = echelleNoteController.getAllTypeEchelles();

        assertEquals(TypeEchelle.values().length, result.size());
    }

    @Test
    @DisplayName("Should not add Echelons when Echelons list is null")
    void ajouterEchelleNoteWithNullEchelons() {
        EchelleNote echelleNote = new EchelleNote();
        echelleNote.setId(1);
        when(echelleNoteService.ajouterEchelle(echelleNote.getTypeEchelle(), echelleNote.getDescription())).thenReturn(echelleNote);

        echelleNoteController.ajouterEchelleNote(echelleNote);

        verify(echelleNoteService, times(1)).ajouterEchelle(echelleNote.getTypeEchelle(), echelleNote.getDescription());
        verify(echelonService, times(0)).save(any(Echelon.class));
    }

    @Test
    @DisplayName("Should return used TypeEchelles")
    void getUsedTypesEchelle() {
        EchelleNote echelleNote1 = new EchelleNote();
        echelleNote1.setTypeEchelle(TypeEchelle.PRESENTATION);
        EchelleNote echelleNote2 = new EchelleNote();
        echelleNote2.setTypeEchelle(TypeEchelle.CONFORMITE);
        when(echelleNoteService.getAllEchelleNotes()).thenReturn(Arrays.asList(echelleNote1, echelleNote2));

        List<TypeEchelle> result = echelleNoteController.getUsedTypesEchelle();

        assertEquals(2, result.size());
        assertTrue(result.contains(TypeEchelle.PRESENTATION));
        assertTrue(result.contains(TypeEchelle.CONFORMITE));
    }

    @Test
    @DisplayName("Should return distinct used TypeEchelles")
    void getUsedTypesEchelleWithDuplicates() {
        EchelleNote echelleNote1 = new EchelleNote();
        echelleNote1.setTypeEchelle(TypeEchelle.CONFORMITE);
        EchelleNote echelleNote2 = new EchelleNote();
        echelleNote2.setTypeEchelle(TypeEchelle.CONFORMITE);
        when(echelleNoteService.getAllEchelleNotes()).thenReturn(Arrays.asList(echelleNote1, echelleNote2));

        List<TypeEchelle> result = echelleNoteController.getUsedTypesEchelle();

        assertEquals(1, result.size());
        assertTrue(result.contains(TypeEchelle.CONFORMITE));
    }

    @Test
    void ajouterEchelleNoteWithNonNullEchelons() {
        // Arrange
        EchelleNote echelleNote = new EchelleNote();
        echelleNote.setId(1);
        Echelon echelonData = new Echelon();
        echelonData.setCommentaire("Test Commentaire");
        echelonData.setNote1(1);
        echelonData.setNote2(2);
        echelleNote.setEchelons(Arrays.asList(echelonData));
        when(echelleNoteService.ajouterEchelle(echelleNote.getTypeEchelle(), echelleNote.getDescription())).thenReturn(echelleNote);

        // Act
        echelleNoteController.ajouterEchelleNote(echelleNote);

        // Assert
        verify(echelleNoteService, times(1)).ajouterEchelle(echelleNote.getTypeEchelle(), echelleNote.getDescription());
        verify(echelonService, times(1)).save(any(Echelon.class));
    }
}