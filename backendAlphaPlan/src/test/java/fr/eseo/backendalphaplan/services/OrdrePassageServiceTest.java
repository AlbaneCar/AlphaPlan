package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.controller.OrdrePassageController.OrdrePassageResponse;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.OrdrePassage;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.EquipeRepository;
import fr.eseo.backendalphaplan.repository.OrdrePassageRepository;
import fr.eseo.backendalphaplan.repository.SprintRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdrePassageServiceTest {

    @Mock
    private OrdrePassageRepository ordrePassageRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private OrdrePassageService ordrePassageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Devrait récupérer l'ordre de passage en fonction de l'id de l'équipe et de l'id du sprint")
    @Test
    void TestGetOrdrePassageByEquipeIdAndSprintId() {
        int sprintId = 1;
        int equipeId = 1;

        Equipe equipe = new Equipe();
        Sprint sprint = new Sprint();
        Utilisateur auteur = new Utilisateur();
        OrdrePassage ordrePassage = new OrdrePassage();
        ordrePassage.setId(1);
        ordrePassage.setEquipe(equipe);
        ordrePassage.setSprint(sprint);
        ordrePassage.setAuteur(auteur);
        ordrePassage.setOrdre(new ArrayList<>());

        when(equipeRepository.existsById(equipeId)).thenReturn(true);
        when(sprintRepository.existsById(sprintId)).thenReturn(true);
        when(ordrePassageRepository.findByEquipeIdAndSprintId(equipeId, sprintId)).thenReturn(ordrePassage);

        OrdrePassageResponse response = ordrePassageService.getOrdrePassageByEquipeIdAndSprintId(sprintId, equipeId);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(equipe, response.getEquipe());
        assertEquals(sprint, response.getSprint());
        assertEquals(auteur, response.getAuteur());
        assertEquals(new ArrayList<>(), response.getOrdre());
    }

    @DisplayName("Devrait lancer une exception si l'id du sprint ou l'id de l'équipe ne sont pas renseignés")
    @Test
    void TestGetOrdreInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> ordrePassageService.getOrdrePassageByEquipeIdAndSprintId(null, 1));
        assertThrows(IllegalArgumentException.class, () -> ordrePassageService.getOrdrePassageByEquipeIdAndSprintId(1, null));
    }

    @DisplayName("Devrait lancer une exception si l'équipe n'existe pas")
    @Test
    void getOrdrePassageByEquipeIdAndSprintIdEquipeNotFoundTest() {
        int sprintId = 1;
        int equipeId = 1;

        when(equipeRepository.existsById(equipeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> ordrePassageService.getOrdrePassageByEquipeIdAndSprintId(sprintId, equipeId));
    }

    @DisplayName("Devrait lancer une exception si le sprint n'existe pas")
    @Test
    void getOrdrePassageByEquipeIdAndSprintIdSprintNotFoundTest() {
        int sprintId = 1;
        int equipeId = 1;

        when(equipeRepository.existsById(equipeId)).thenReturn(true);
        when(sprintRepository.existsById(sprintId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> ordrePassageService.getOrdrePassageByEquipeIdAndSprintId(sprintId, equipeId));
    }

    @DisplayName("Devrait lancer une exception si l'ordre de passage n'existe pas")
    @Test
    void getOrdrePassageByEquipeIdAndSprintIdOrdrePassageNotFoundTest() {
        int sprintId = 1;
        int equipeId = 1;

        when(equipeRepository.existsById(equipeId)).thenReturn(true);
        when(sprintRepository.existsById(sprintId)).thenReturn(true);
        when(ordrePassageRepository.findByEquipeIdAndSprintId(equipeId, sprintId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> ordrePassageService.getOrdrePassageByEquipeIdAndSprintId(sprintId, equipeId));
    }

    @DisplayName("Devrait créer un ordre de passage")
    @Test
    void createOrdrePassageTest() {
        int sprintId = 1;
        int equipeId = 1;
        int auteurId = 1;
        List<Integer> ordre = List.of(1, 2, 3);

        Utilisateur auteur = new Utilisateur();
        Sprint sprint = new Sprint();
        Equipe equipe = new Equipe();
        Utilisateur utilisateur1 = new Utilisateur();
        Utilisateur utilisateur2 = new Utilisateur();
        Utilisateur utilisateur3 = new Utilisateur();

        when(utilisateurRepository.findById(auteurId)).thenReturn(Optional.of(auteur));
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));
        when(equipeRepository.findById(equipeId)).thenReturn(Optional.of(equipe));
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(utilisateur1));
        when(utilisateurRepository.findById(2)).thenReturn(Optional.of(utilisateur2));
        when(utilisateurRepository.findById(3)).thenReturn(Optional.of(utilisateur3));
        when(ordrePassageRepository.findByEquipeIdAndSprintId(equipeId, sprintId)).thenReturn(null);

        assertDoesNotThrow(() -> ordrePassageService.createOrdrePassage(sprintId, equipeId, ordre, auteurId));

        verify(ordrePassageRepository, times(1)).save(any(OrdrePassage.class));
    }

    @DisplayName("Devrait lancer une exception si les champs ne sont pas renseignés")
    @Test
    void createOrdrePassageInvalidParamsTest() {
        List<Integer> ordre = List.of(1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> ordrePassageService.createOrdrePassage(null, 1, ordre, 1));
        assertThrows(IllegalArgumentException.class, () -> ordrePassageService.createOrdrePassage(1, null, ordre, 1));
        assertThrows(IllegalArgumentException.class, () -> ordrePassageService.createOrdrePassage(1, 1, null, 1));
        assertThrows(IllegalArgumentException.class, () -> ordrePassageService.createOrdrePassage(1, 1, ordre, null));
    }

    @DisplayName("Devrait lancer une exception si les éléments n'existent pas")
    @Test
    void createOrdrePassageElementsNotFoundTest() {
        int sprintId = 1;
        int equipeId = 1;
        int auteurId = 1;
        List<Integer> ordre = List.of(1, 2, 3);

        when(utilisateurRepository.findById(auteurId)).thenReturn(Optional.empty());
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());
        when(equipeRepository.findById(equipeId)).thenReturn(Optional.empty());
        when(utilisateurRepository.findById(1)).thenReturn(Optional.empty());
        when(utilisateurRepository.findById(2)).thenReturn(Optional.empty());
        when(utilisateurRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> ordrePassageService.createOrdrePassage(sprintId, equipeId, ordre, auteurId));
    }
}
