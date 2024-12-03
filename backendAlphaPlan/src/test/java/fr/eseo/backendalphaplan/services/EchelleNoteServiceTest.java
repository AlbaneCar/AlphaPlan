package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.EchelleNote;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;
import fr.eseo.backendalphaplan.repository.EchelleNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class EchelleNoteServiceTest {

    @InjectMocks
    EchelleNoteService echelleNoteService;

    @Mock
    EchelleNoteRepository echelleNoteRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return EchelleNote when found by id")
     void testGetEchelleNoteById() {
        EchelleNote echelleNote = new EchelleNote();
        echelleNote.setId(1);
        when(echelleNoteRepository.findEchelleById(1)).thenReturn(echelleNote);

        EchelleNote result = echelleNoteService.getEchelleNoteById(1);

        assertEquals(echelleNote, result);
    }

    @Test
    @DisplayName("Should return EchelleNote when found by type")
     void testGetEchelleNoteByType() {
        EchelleNote echelleNote = new EchelleNote();
        echelleNote.setTypeEchelle(TypeEchelle.TECHNIQUE);
        when(echelleNoteRepository.findByTypeEchelle(TypeEchelle.TECHNIQUE)).thenReturn(echelleNote);

        EchelleNote result = echelleNoteService.getEchelleNoteByTyep(TypeEchelle.TECHNIQUE);

        assertEquals(echelleNote, result);
    }

    @Test
    @DisplayName("Should add and return new EchelleNote")
     void testAjouterEchelle() {
        EchelleNote echelleNote = new EchelleNote();
        echelleNote.setTypeEchelle(TypeEchelle.TECHNIQUE);
        echelleNote.setDescription("description");
        when(echelleNoteRepository.save(any(EchelleNote.class))).thenReturn(echelleNote);

        EchelleNote result = echelleNoteService.ajouterEchelle(TypeEchelle.TECHNIQUE, "description");

        assertEquals(echelleNote, result);
    }

    @Test
    @DisplayName("Should modify and return EchelleNote")
     void testModifierEchelle() {
        EchelleNote echelleNote = new EchelleNote();
        echelleNote.setId(1);
        echelleNote.setTypeEchelle(TypeEchelle.TECHNIQUE);
        echelleNote.setDescription("description");

        when(echelleNoteRepository.findEchelleById(1)).thenReturn(echelleNote);
        when(echelleNoteRepository.save(any(EchelleNote.class))).thenReturn(echelleNote);

        EchelleNote result = echelleNoteService.modifierEchelle(1, TypeEchelle.TECHNIQUE, "description");

        assertEquals(echelleNote, result);
    }

    @Test
    @DisplayName("Should throw RuntimeException when EchelleNote not found")
     void testModifierEchelleNotFound() {
        when(echelleNoteRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> echelleNoteService.modifierEchelle(1, TypeEchelle.TECHNIQUE, "description"));
    }

    @Test
    @DisplayName("Should delete EchelleNote by id")
     void testSupprimerEchelle() {
        doNothing().when(echelleNoteRepository).deleteById(1);

        echelleNoteService.supprimerEchelle(1);

        verify(echelleNoteRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should return all EchelleNotes")
     void testGetAllEchelleNotes() {
        EchelleNote echelleNote1 = new EchelleNote();
        EchelleNote echelleNote2 = new EchelleNote();
        List<EchelleNote> echelleNotes = Arrays.asList(echelleNote1, echelleNote2);
        when(echelleNoteRepository.findAll()).thenReturn(echelleNotes);

        List<EchelleNote> result = echelleNoteService.getAllEchelleNotes();

        assertEquals(echelleNotes, result);
    }
}