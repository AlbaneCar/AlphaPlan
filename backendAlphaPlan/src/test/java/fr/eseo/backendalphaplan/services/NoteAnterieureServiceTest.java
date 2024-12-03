package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.NoteAnterieure;
import fr.eseo.backendalphaplan.model.enums.Matiere;
import fr.eseo.backendalphaplan.repository.NoteAnterieureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NoteAnterieureServiceTest {

    @InjectMocks
    private NoteAnterieureService noteAnterieureService;

    @Mock
    private NoteAnterieureRepository noteAnterieureRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMoyenneEquipe() {
        when(noteAnterieureRepository.getMoyenneEquipe(anyInt())).thenReturn(10.0);

        double result = noteAnterieureService.getMoyenneEquipe(1);

        assertEquals(10.0, result);
    }

    @Test
    void testGetMoyenneUtilisateur() {
        when(noteAnterieureRepository.findAllByUtilisateur_Id(anyInt())).thenReturn(Collections.emptyList());

        double result = noteAnterieureService.getMoyenneUtilisateur(1);

        assertEquals(0.0, result);
    }

    @Test
    void testModifierCoefMatiere() {
        NoteAnterieure note = new NoteAnterieure();
        note.setCoef(1.0f);
        when(noteAnterieureRepository.findByMatiere(any(Matiere.class))).thenReturn(Collections.singletonList(note));

        noteAnterieureService.modifierCoefMatiere(Matiere.IRS, 2.0f);

        assertEquals(2.0f, note.getCoef());
    }

    @Test
    void testGetAllNotesAnterieures() {
        NoteAnterieure note = new NoteAnterieure();
        when(noteAnterieureRepository.findAllByUtilisateur_Id(anyInt())).thenReturn(Collections.singletonList(note));

        var result = noteAnterieureService.getAllNotesAnterieures(1);

        assertEquals(1, result.size());
    }

    @Test
    void testGetCoefMatiere() {
        when(noteAnterieureRepository.getCoef(any(Matiere.class))).thenReturn(1);

        int result = noteAnterieureService.getCoefMatiere(Matiere.PADL);

        assertEquals(1, result);
    }
}