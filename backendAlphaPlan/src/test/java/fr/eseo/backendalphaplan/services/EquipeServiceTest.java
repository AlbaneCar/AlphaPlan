package fr.eseo.backendalphaplan.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.EtatEquipes;
import fr.eseo.backendalphaplan.model.enums.Genre;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;
import fr.eseo.backendalphaplan.repository.EquipeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class EquipeServiceTest {

    @InjectMocks
    private EquipeService equipeService;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private NoteAnterieureService noteAnterieureService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEquipeById() {
        Equipe equipe = new Equipe();
        when(equipeRepository.findById(anyInt())).thenReturn(Optional.of(equipe));

        Equipe result = equipeService.getEquipeById(1);

        assertEquals(equipe, result);
    }

    @Test
    void testGetEquipeByIdNotFound() {
        when(equipeRepository.findById(anyInt())).thenReturn(Optional.empty());

        Equipe result = equipeService.getEquipeById(1);

        assertNull(result);
    }

    @Test
    void testGetEquipeByNom() {
        Equipe equipe = new Equipe();
        when(equipeRepository.findByNom(anyString())).thenReturn(equipe);

        Equipe result = equipeService.getEquipeByNom("Equipe1");

        assertEquals(equipe, result);
    }

    @Test
    void testGetAllEquipes() {
        Equipe equipe1 = new Equipe();
        Equipe equipe2 = new Equipe();
        when(equipeRepository.findAll()).thenReturn(Arrays.asList(equipe1, equipe2));

        List<Equipe> result = equipeService.getAllEquipes();

        assertEquals(2, result.size());
    }

    @Test
    void testSaveEquipe() {
        Equipe equipe = new Equipe();
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.saveEquipe(equipe);

        verify(equipeRepository, times(1)).save(equipe);
        assertEquals(equipe, result);
    }

    @Test
    void testDeleteEquipe() {
        equipeService.deleteEquipe();

        verify(equipeRepository, times(1)).deleteAll();
    }

    @Test
    void testGetStudentsByEquipeId() {
        Utilisateur utilisateur = new Utilisateur();
        when(equipeRepository.getStudentsByEquipeId(anyInt())).thenReturn(Arrays.asList(utilisateur));

        List<Utilisateur> result = equipeService.getStudentsByEquipeId(1);

        assertEquals(1, result.size());
        assertEquals(utilisateur, result.get(0));
    }

    @Test
    void testCreerEquipes() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setGenre(Genre.HOMME);
        utilisateur.setTypeUtilisateur(TypeUtilisateur.E3E);

        when(utilisateurService.getEleves()).thenReturn(Arrays.asList(utilisateur, utilisateur, utilisateur));
        when(noteAnterieureService.getMoyenneUtilisateur(anyInt())).thenReturn(10.0);

        List<List<Utilisateur>> result = equipeService.creerEquipes(1);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
    }

    @Test
    void testRemoveAllEquipes() {
        Equipe equipe = new Equipe();
        TypedQuery<Equipe> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Equipe.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(equipe));

        equipeService.removeAllEquipes();

        verify(entityManager, times(1)).remove(any(Equipe.class));
    }

    @Test
    void testAssignerUtilisateurEquipe() {
        Utilisateur utilisateur = new Utilisateur();
        Equipe equipe = new Equipe();

        when(utilisateurService.getUtilisateurById(anyInt())).thenReturn(utilisateur);
        when(entityManager.find(eq(Equipe.class), anyInt())).thenReturn(equipe);

        equipeService.assignerUtilisateurAEquipe(1, 1);

        verify(entityManager, times(1)).merge(any(Utilisateur.class));
    }

    @Test
    void testAssignUsers() throws Exception {
        String json = "[{\"members\": [{\"id\": 1}], \"name\": \"Equipe1\", \"staff\": {\"id\": 1}}]";
        JsonNode jsonNode = mock(JsonNode.class);
        JsonNode equipeNode = mock(JsonNode.class);
        JsonNode membersNode = mock(JsonNode.class);
        JsonNode memberNode = mock(JsonNode.class);
        JsonNode staffNode = mock(JsonNode.class);

        when(objectMapper.readTree(json)).thenReturn(jsonNode);
        when(jsonNode.iterator()).thenReturn(Arrays.asList(equipeNode).iterator());
        when(equipeNode.get("name")).thenReturn(mock(JsonNode.class));
        when(equipeNode.get("staff")).thenReturn(staffNode);
        when(equipeNode.get("members")).thenReturn(membersNode);
        when(staffNode.get("id")).thenReturn(mock(JsonNode.class));
        when(membersNode.iterator()).thenReturn(Arrays.asList(memberNode).iterator());
        when(memberNode.get("id")).thenReturn(mock(JsonNode.class));

        equipeService.processJsonAndAssignUsers(json, EtatEquipes.PUBLIE);

        verify(entityManager, times(1)).persist(any(Equipe.class));
        verify(entityManager, times(1)).flush();
    }

    @Test
    void testChangementEtatEquipes() {
        Equipe equipe = new Equipe();
        when(equipeRepository.findAll()).thenReturn(Arrays.asList(equipe));

        equipeService.changeTeamsState(EtatEquipes.PUBLIE);

        verify(entityManager, times(1)).merge(any(Equipe.class));
        verify(entityManager, times(1)).flush();
    }
}
