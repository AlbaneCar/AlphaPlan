package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.EchelleNote;
import fr.eseo.backendalphaplan.model.Echelon;
import fr.eseo.backendalphaplan.repository.EchelonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EchelonServiceTest {

    @InjectMocks
    EchelonService echelonService;

    @Mock
    EchelonRepository echelonRepository;

    @BeforeEach
     void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void getEchelonsByEchelleNoteIdReturnsExpectedEchelons() {
        EchelleNote echelleNote = new EchelleNote();
        Echelon echelon1 = new Echelon();
        Echelon echelon2 = new Echelon();
        List<Echelon> expectedEchelons = Arrays.asList(echelon1, echelon2);

        when(echelonRepository.findByEchelleNoteId(echelleNote)).thenReturn(expectedEchelons);

        List<Echelon> actualEchelons = echelonService.getEchelonsByEchelleNoteId(echelleNote);

        assertEquals(expectedEchelons, actualEchelons);
    }

    @Test
     void saveCallsRepositorySave() {
        Echelon echelon = new Echelon();

        echelonService.save(echelon);

        verify(echelonRepository, times(1)).save(echelon);
    }

    @Test
    void deleteCallsRepositoryDelete() {
        Echelon echelon = new Echelon();

        echelonService.delete(echelon);

        verify(echelonRepository, times(1)).delete(echelon);
    }
}