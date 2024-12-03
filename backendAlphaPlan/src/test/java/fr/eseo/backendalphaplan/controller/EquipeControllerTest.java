package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.enums.EtatEquipes;
import fr.eseo.backendalphaplan.services.EquipeService;
import fr.eseo.backendalphaplan.controller.EquipeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EquipeControllerTest {

    @InjectMocks
    private EquipeController equipeController;

    @Mock
    private EquipeService equipeService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEquipe() {
        Equipe equipe = new Equipe();
        when(equipeService.saveEquipe(equipe)).thenReturn(equipe);

        Equipe result = equipeController.createEquipe(equipe);

        assertEquals(equipe, result);
        verify(equipeService, times(1)).saveEquipe(equipe);
    }

    @Test
    void testGetEquipeById() {
        Equipe equipe = new Equipe();
        when(equipeService.getEquipeById(1)).thenReturn(equipe);

        ResponseEntity<Equipe> result = equipeController.getEquipeById(1);

        assertEquals(equipe, result.getBody());
        verify(equipeService, times(1)).getEquipeById(1);
    }

    @Test
    void testGetAllEquipes() {
        Equipe equipe = new Equipe();
        when(equipeService.getAllEquipes()).thenReturn(List.of(equipe));

        List<Equipe> result = equipeController.getAllEquipes();

        assertEquals(1, result.size());
        verify(equipeService, times(1)).getAllEquipes();
    }

    @Test
    void testUpdateEquipe() {
        Equipe equipe = new Equipe();
        Equipe updatedEquipe = new Equipe();
        updatedEquipe.setNom("Updated Name");

        when(equipeService.getEquipeById(1)).thenReturn(equipe);
        when(equipeService.saveEquipe(equipe)).thenReturn(updatedEquipe);

        ResponseEntity<Equipe> result = equipeController.updateEquipe(1, updatedEquipe);

        assertEquals(updatedEquipe, result.getBody());
        verify(equipeService, times(1)).getEquipeById(1);
        verify(equipeService, times(1)).saveEquipe(equipe);
    }

    @Test
    void testGetEquipeByIdException() {
        when(equipeService.getEquipeById(1)).thenReturn(null);

        ResponseEntity<Equipe> result = equipeController.getEquipeById(1);

        assertEquals(404, result.getStatusCodeValue());
        verify(equipeService, times(1)).getEquipeById(1);
    }

    @Test
    void testUpdateEquipeException() {
        Equipe updatedEquipe = new Equipe();
        updatedEquipe.setNom("Updated Name");

        when(equipeService.getEquipeById(1)).thenReturn(null);

        ResponseEntity<Equipe> result = equipeController.updateEquipe(1, updatedEquipe);

        assertEquals(404, result.getStatusCodeValue());
        verify(equipeService, times(1)).getEquipeById(1);
    }

    @Test
    void createEquipeReturnsSavedEquipe() {
        Equipe equipe = new Equipe();
        when(equipeService.saveEquipe(any(Equipe.class))).thenReturn(equipe);

        Equipe result = equipeController.createEquipe(new Equipe());

        assertEquals(equipe, result);
        verify(equipeService, times(1)).saveEquipe(any(Equipe.class));
    }

    @Test
    void getEquipeByIdReturnsNotFoundWhenEquipeDoesNotExist() {
        when(equipeService.getEquipeById(anyInt())).thenReturn(null);

        ResponseEntity<Equipe> result = equipeController.getEquipeById(1);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void getAllEquipesReturnsEmptyListWhenNoEquipesExist() {
        when(equipeService.getAllEquipes()).thenReturn(Collections.emptyList());

        List<Equipe> result = equipeController.getAllEquipes();

        assertTrue(result.isEmpty());
    }

    @Test
    void updateEquipeReturnsNotFoundWhenEquipeDoesNotExist() {
        when(equipeService.getEquipeById(anyInt())).thenReturn(null);

        ResponseEntity<Equipe> result = equipeController.updateEquipe(1, new Equipe());

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void supprimerEquipesInvokesRemoveAllEquipes() {
        doNothing().when(equipeService).removeAllEquipes();

        ResponseEntity<Void> result = equipeController.supprimerEquipes();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(equipeService, times(1)).removeAllEquipes();
    }



}