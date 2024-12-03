package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.ValidationBM;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import fr.eseo.backendalphaplan.services.ValidationBMService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationBMControllerTest {

    @Mock
    private ValidationBMService validationBMService;

    @InjectMocks
    private ValidationBMController validationBMController;

    @Test
    void testAjouterValidation() {
        // Création ValidationBM
        ValidationBM validationBM = new ValidationBM();

        // Mock du service
        when(validationBMService.saveValidationBM(validationBM)).thenReturn(validationBM);

        // Méthode à tester
        ResponseEntity<ValidationBM> response = validationBMController.ajouterValidation(validationBM);

        // Vérifications
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(validationBM, response.getBody());
        verify(validationBMService, times(1)).saveValidationBM(validationBM);
    }

    @Test
    void testAjouterValidation_Echec() {
        ValidationBM validationBM = new ValidationBM();

        // Echec d'insertion (car duplication de ValidationBM par exemple)
        when(validationBMService.saveValidationBM(validationBM)).thenReturn(null);

        ResponseEntity<ValidationBM> response = validationBMController.ajouterValidation(validationBM);

        // Vérifications
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(validationBMService, times(1)).saveValidationBM(validationBM);
    }

    @Test
    void testAjouterValidation_Exception() {
        ValidationBM validationBM = new ValidationBM(/* mettre les paramètres appropriés */);

        // Exception catch
        when(validationBMService.saveValidationBM(validationBM)).thenThrow(new RuntimeException());

        ResponseEntity<ValidationBM> response = validationBMController.ajouterValidation(validationBM);

        // Vérifications
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(validationBMService, times(1)).saveValidationBM(validationBM);
    }

    @Test
    void testGetNbValidationsBM() {
        // Prepare test data
        Integer id = 1;
        TypeNoteEleve type = TypeNoteEleve.IN_SP;
        int sprint = 1;
        int equipe = 1;
        int expected = 5;

        // Mock the behavior of validationBMService
        when(validationBMService.getNbValidationsBM(id, type, sprint, equipe)).thenReturn(expected);

        // Call the method under test
        int result = validationBMController.getNbValidationsBM(id, type, sprint, equipe);

        // Verify the results
        assertEquals(expected, result);

        // Verify the interactions with the mock
        verify(validationBMService, times(1)).getNbValidationsBM(id, type, sprint, equipe);
    }


}
