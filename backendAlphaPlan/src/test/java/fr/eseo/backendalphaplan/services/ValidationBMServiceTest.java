package fr.eseo.backendalphaplan.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.eseo.backendalphaplan.model.BonusMalus;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.ValidationBM;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import fr.eseo.backendalphaplan.repository.ValidationBMRepository;
import fr.eseo.backendalphaplan.services.ValidationBMService;

class ValidationBMServiceTest {

    @Mock
    private ValidationBMRepository validationBMRepository;

    @InjectMocks
    private ValidationBMService validationBMService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveValidationBM_Success() {
        BonusMalus bonusMalus = new BonusMalus();
        Utilisateur utilisateur = new Utilisateur();
        ValidationBM validationBM = new ValidationBM();
        validationBM.setBonusMalus(bonusMalus);
        validationBM.setUtilisateur(utilisateur);
        when(validationBMRepository.existsByBonusMalusAndUtilisateur(bonusMalus, utilisateur)).thenReturn(false);
        when(validationBMRepository.save(any(ValidationBM.class))).thenReturn(validationBM);

        ValidationBM result = validationBMService.saveValidationBM(validationBM);

        assertNotNull(result);
        assertEquals(bonusMalus, result.getBonusMalus());
        assertEquals(utilisateur, result.getUtilisateur());
    }

    @Test
    void testSaveValidationBM_Failure() {
        BonusMalus bonusMalus = new BonusMalus();
        Utilisateur utilisateur = new Utilisateur();
        ValidationBM validationBM = new ValidationBM();
        validationBM.setBonusMalus(bonusMalus);
        validationBM.setUtilisateur(utilisateur);
        when(validationBMRepository.existsByBonusMalusAndUtilisateur(bonusMalus, utilisateur)).thenReturn(true);

        ValidationBM result = validationBMService.saveValidationBM(validationBM);

        assertNull(result);
    }

    @Test
    void testGetNbValidationsBM() {
        Integer id = 1;
        TypeNoteEleve type = TypeNoteEleve.IN_SP;
        int sprint = 1;
        int equipe = 1;
        int nbValidations = 3;
        when(validationBMRepository.getNbValidationsBM(id, type, sprint, equipe)).thenReturn(nbValidations);

        int result = validationBMService.getNbValidationsBM(id, type, sprint, equipe);

        assertEquals(nbValidations, result);
    }
}
