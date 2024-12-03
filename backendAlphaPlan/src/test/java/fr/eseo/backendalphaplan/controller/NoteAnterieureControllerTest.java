package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.enums.Matiere;
import fr.eseo.backendalphaplan.services.NoteAnterieureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NoteAnterieureControllerTest {

    @InjectMocks
    private NoteAnterieureController noteAnterieureController;

    @Mock
    private NoteAnterieureService noteAnterieureService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMoyenneEquipe() {
        when(noteAnterieureService.getMoyenneEquipe(1)).thenReturn(10.0);

        double result = noteAnterieureController.getMoyenneEquipe(1);

        assertEquals(10.0, result);
        verify(noteAnterieureService, times(1)).getMoyenneEquipe(1);
    }

    @Test
    void testGetMoyenneUtilisateur() {
        when(noteAnterieureService.getMoyenneUtilisateur(1)).thenReturn(10.0);

        double result = noteAnterieureController.getMoyenneUtilisateur(1);

        assertEquals(10.0, result);
        verify(noteAnterieureService, times(1)).getMoyenneUtilisateur(1);
    }

    @Test
    void testModifierCoefMatiere() {
        doNothing().when(noteAnterieureService).modifierCoefMatiere(Matiere.IRS, 2.0f);

        ResponseEntity<Void> result = noteAnterieureController.modifierCoefMatiere(Matiere.IRS, 2.0f);

        assertEquals(200, result.getStatusCodeValue());
        verify(noteAnterieureService, times(1)).modifierCoefMatiere(Matiere.IRS, 2.0f);
    }

    @Test
    void testGetAllMatieres() {
        List<Matiere> expected = Arrays.asList(Matiere.values());

        List<Matiere> result = noteAnterieureController.getAllMatieres();

        assertEquals(expected, result);
    }

    @Test
    void testGetCoefMatiere() {
        when(noteAnterieureService.getCoefMatiere(Matiere.IRS)).thenReturn(2);

        ResponseEntity<Integer> result = noteAnterieureController.getCoefMatiere(Matiere.IRS);

        assertEquals(2, result.getBody());
        verify(noteAnterieureService, times(1)).getCoefMatiere(Matiere.IRS);
    }

    @Test
    void testGetCoefMatiere_Exception() {
        when(noteAnterieureService.getCoefMatiere(Matiere.IRS)).thenThrow(new RuntimeException());

        ResponseEntity<Integer> result = noteAnterieureController.getCoefMatiere(Matiere.IRS);

        assertEquals(400, result.getStatusCodeValue());
        verify(noteAnterieureService, times(1)).getCoefMatiere(Matiere.IRS);
    }

    @Test
    void getMoyenneEquipeReturnsExpectedAverage() {
        when(noteAnterieureService.getMoyenneEquipe(1)).thenReturn(15.0);

        double result = noteAnterieureController.getMoyenneEquipe(1);

        assertEquals(15.0, result);
        verify(noteAnterieureService, times(1)).getMoyenneEquipe(1);
    }

    @Test
    void getMoyenneUtilisateurReturnsExpectedAverage() {
        when(noteAnterieureService.getMoyenneUtilisateur(2)).thenReturn(12.0);

        double result = noteAnterieureController.getMoyenneUtilisateur(2);

        assertEquals(12.0, result);
        verify(noteAnterieureService, times(1)).getMoyenneUtilisateur(2);
    }

    @Test
    void modifierCoefMatiereReturnsOkStatus() {
        doNothing().when(noteAnterieureService).modifierCoefMatiere(Matiere.IRS, 3.0f);

        ResponseEntity<Void> result = noteAnterieureController.modifierCoefMatiere(Matiere.IRS, 3.0f);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(noteAnterieureService, times(1)).modifierCoefMatiere(Matiere.IRS, 3.0f);
    }

    @Test
    void getAllMatieresReturnsExpectedMatieres() {
        List<Matiere> expected = Arrays.asList(Matiere.values());

        List<Matiere> result = noteAnterieureController.getAllMatieres();

        assertEquals(expected, result);
    }

    @Test
    void getCoefMatiereReturnsExpectedCoef() {
        when(noteAnterieureService.getCoefMatiere(Matiere.IRS)).thenReturn(3);

        ResponseEntity<Integer> result = noteAnterieureController.getCoefMatiere(Matiere.IRS);

        assertEquals(3, result.getBody());
        verify(noteAnterieureService, times(1)).getCoefMatiere(Matiere.IRS);
    }

}