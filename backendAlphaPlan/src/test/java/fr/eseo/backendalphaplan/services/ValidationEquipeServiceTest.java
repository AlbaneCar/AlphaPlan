package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.ValidationEquipes;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import fr.eseo.backendalphaplan.repository.ValidationEquipesRepository;
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

class ValidationEquipesServiceTest {

    @Mock
    private ValidationEquipesRepository validationEquipesRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private ValidationEquipesService validationEquipesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Devrait créer une ValidationEquipe")
    @Test
    void createValidationEquipeTest() {
        int utilisateurId = 1;
        Utilisateur utilisateur = new Utilisateur();
        ValidationEquipes validationEquipes = new ValidationEquipes();
        validationEquipes.setUtilisateur(utilisateur);

        when(utilisateurRepository.findById(utilisateurId)).thenReturn(Optional.of(utilisateur));
        when(validationEquipesRepository.save(any(ValidationEquipes.class))).thenReturn(validationEquipes);

        ValidationEquipes result = validationEquipesService.createValidationEquipe(utilisateurId);

        assertNotNull(result);
        assertEquals(utilisateur, result.getUtilisateur());
        verify(utilisateurRepository, times(1)).findById(utilisateurId);
        verify(validationEquipesRepository, times(1)).save(any(ValidationEquipes.class));
    }

    @DisplayName("Devrait lancer une exception si l'utilisateur n'est pas trouvé")
    @Test
    void createValidationEquipeUserNotFoundTest() {
        int utilisateurId = 1;

        when(utilisateurRepository.findById(utilisateurId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validationEquipesService.createValidationEquipe(utilisateurId);
        });

        String expectedMessage = "Utilisateur not found with id: " + utilisateurId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(utilisateurRepository, times(1)).findById(utilisateurId);
        verify(validationEquipesRepository, times(0)).save(any(ValidationEquipes.class));
    }

    @DisplayName("Devrait récupérer toutes les ValidationEquipes")
    @Test
    void getAllValidationEquipesTest() {
        ValidationEquipes validationEquipes1 = new ValidationEquipes();
        ValidationEquipes validationEquipes2 = new ValidationEquipes();

        when(validationEquipesRepository.findAll()).thenReturn(List.of(validationEquipes1, validationEquipes2));

        List<ValidationEquipes> result = validationEquipesService.getAllValidationEquipes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(validationEquipes1));
        assertTrue(result.contains(validationEquipes2));
        verify(validationEquipesRepository, times(1)).findAll();
    }

    @DisplayName("Devrait supprimer toutes les ValidationEquipes")
    @Test
    void deleteAllValidationEquipesTest() {
        doNothing().when(validationEquipesRepository).deleteAll();

        validationEquipesService.deleteAllValidationEquipes();

        verify(validationEquipesRepository, times(1)).deleteAll();
    }
}
