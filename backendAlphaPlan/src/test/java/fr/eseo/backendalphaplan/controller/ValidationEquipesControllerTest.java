package fr.eseo.backendalphaplan.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import fr.eseo.backendalphaplan.model.ValidationEquipes;
import fr.eseo.backendalphaplan.services.EquipeService;
import fr.eseo.backendalphaplan.services.ValidationEquipesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

class ValidationEquipesControllerTest {

    @InjectMocks
    private ValidationEquipesController validationEquipesController;

    @Mock
    private ValidationEquipesService validationEquipesService;

    @Mock
    private EquipeService equipeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllValidationEquipesReturnsExpectedResult() {
        ValidationEquipes validationEquipes1 = new ValidationEquipes();
        ValidationEquipes validationEquipes2 = new ValidationEquipes();
        List<ValidationEquipes> validationEquipesList = Arrays.asList(validationEquipes1, validationEquipes2);

        when(validationEquipesService.getAllValidationEquipes()).thenReturn(validationEquipesList);

        ResponseEntity<List<ValidationEquipes>> result = validationEquipesController.getAllValidationEquipes();

        assertEquals(validationEquipesList, result.getBody());
        verify(validationEquipesService, times(1)).getAllValidationEquipes();
    }

    @Test
    void createValidationEquipeReturnsExpectedResult() {
        ValidationEquipes validationEquipes = new ValidationEquipes();
        Integer utilisateurId = 1;

        when(validationEquipesService.createValidationEquipe(utilisateurId)).thenReturn(validationEquipes);

        ResponseEntity<ValidationEquipes> result = validationEquipesController.createValidationEquipe(utilisateurId);

        assertEquals(validationEquipes, result.getBody());
        verify(validationEquipesService, times(1)).createValidationEquipe(utilisateurId);
    }

    @Test
    void createValidationEquipeHandlesIllegalArgumentException() {
        Integer utilisateurId = 1;

        when(validationEquipesService.createValidationEquipe(utilisateurId)).thenThrow(IllegalArgumentException.class);

        ResponseEntity<ValidationEquipes> result = validationEquipesController.createValidationEquipe(utilisateurId);

        assertEquals(ResponseEntity.badRequest().body(null), result);
        verify(validationEquipesService, times(1)).createValidationEquipe(utilisateurId);
    }

    @Test
    void deleteAllValidationEquipesExecutesExpectedMethods() {
        doNothing().when(validationEquipesService).deleteAllValidationEquipes();
        doNothing().when(equipeService).changeTeamsState(any());

        ResponseEntity<Void> result = validationEquipesController.deleteAllValidationEquipes();

        assertEquals(ResponseEntity.noContent().build(), result);
        verify(validationEquipesService, times(1)).deleteAllValidationEquipes();
        verify(equipeService, times(1)).changeTeamsState(any());
    }
}
