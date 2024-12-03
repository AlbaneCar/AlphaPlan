package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.controller.LineUpController;
import fr.eseo.backendalphaplan.model.LineUp;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.ValidationLineUp;
import fr.eseo.backendalphaplan.model.enums.StatusLineUp;
import fr.eseo.backendalphaplan.repository.LineUpRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import fr.eseo.backendalphaplan.repository.ValidationLineUpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationLineUpServiceTest {

    @InjectMocks
    private ValidationLineUpService validationLineUpService;

    @Mock
    private ValidationLineUpRepository validationLineUpRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private LineUpRepository lineUpRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should analyse request and update validation status")
    @Test
    void analyseRequest() {
        LineUpController.ValidationRequest req = new LineUpController.ValidationRequest(1, 1, true);
        Utilisateur utilisateur = new Utilisateur();
        LineUp lineUp = new LineUp();
        ValidationLineUp validationLineUp = new ValidationLineUp();

        when(utilisateurRepository.findById(req.getAuteur())).thenReturn(Optional.of(utilisateur));
        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.of(lineUp));
        when(validationLineUpRepository.findByLineUpAndUtilisateur(lineUp, utilisateur)).thenReturn(validationLineUp);

        validationLineUpService.analyseRequest(req);

        assertEquals(StatusLineUp.ACCEPTE, validationLineUp.getStatus());
        verify(validationLineUpRepository, times(1)).save(validationLineUp);
    }

    @DisplayName("Should throw IllegalArgumentException when request attributes are null")
    @Test
    void analyseRequestWithNullAttributes() {
        LineUpController.ValidationRequest req = new LineUpController.ValidationRequest(null, null, null);

        assertThrows(IllegalArgumentException.class, () -> validationLineUpService.analyseRequest(req));
    }

    @DisplayName("Should throw IllegalArgumentException when instances do not exist")
    @Test
    void analyseRequestWithNonExistingInstances() {
        LineUpController.ValidationRequest req = new LineUpController.ValidationRequest(1, 1, true);

        when(utilisateurRepository.findById(req.getAuteur())).thenReturn(Optional.empty());
        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> validationLineUpService.analyseRequest(req));
    }

    @DisplayName("Should return list of validations for a given lineUpId")
    @Test
    void getStatusOf() {
        Integer lineUpId = 1;
        LineUp lineUp = new LineUp();
        ValidationLineUp validationLineUp1 = new ValidationLineUp();
        ValidationLineUp validationLineUp2 = new ValidationLineUp();

        when(lineUpRepository.findById(lineUpId)).thenReturn(Optional.of(lineUp));
        when(validationLineUpRepository.findAllByLineUp(lineUp)).thenReturn(List.of(validationLineUp1, validationLineUp2));

        List<ValidationLineUp> result = validationLineUpService.getStatusOf(lineUpId);

        assertEquals(2, result.size());
        assertTrue(result.contains(validationLineUp1));
        assertTrue(result.contains(validationLineUp2));
    }

    @DisplayName("Should throw IllegalArgumentException when lineUpId is null")
    @Test
    void getStatusOfWithNullLineUpId() {
        assertThrows(IllegalArgumentException.class, () -> validationLineUpService.getStatusOf(null));
    }

    @DisplayName("Should throw IllegalArgumentException when lineUp does not exist")
    @Test
    void getStatusOfWithNonExistingLineUp() {
        Integer lineUpId = 1;

        when(lineUpRepository.findById(lineUpId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> validationLineUpService.getStatusOf(lineUpId));
    }
}