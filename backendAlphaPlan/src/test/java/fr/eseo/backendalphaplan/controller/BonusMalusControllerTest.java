package fr.eseo.backendalphaplan.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.eseo.backendalphaplan.controller.BonusMalusController;
import fr.eseo.backendalphaplan.model.BonusMalus;
import fr.eseo.backendalphaplan.model.NoteEleve;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.services.BonusMalusService;

@ExtendWith(MockitoExtension.class)
class BonusMalusControllerTest {

    @Mock
    private BonusMalusService bonusMalusService;

    @InjectMocks
    private BonusMalusController bonusMalusController;

    private List<BonusMalus> bonusMalusList;

    @BeforeEach
    void setUp() {
        bonusMalusList = new ArrayList<>();
        bonusMalusList.add(createBonusMalus(3, 0, createNoteEleve(2), createUtilisateur(1), "Commentaire"));
    }
    private BonusMalus createBonusMalus(Integer id, float valeur, NoteEleve noteEleve, Utilisateur evaluateur, String commentaire) {
        BonusMalus bonusMalus = new BonusMalus();
        bonusMalus.setId(id);
        bonusMalus.setValeur(valeur);
        bonusMalus.setNoteEleve(noteEleve);
        bonusMalus.setEvaluateur(evaluateur);
        bonusMalus.setCommentaire(commentaire);
        return bonusMalus;
    }

    private NoteEleve createNoteEleve(Integer id) {
        NoteEleve noteEleve = new NoteEleve();
        noteEleve.setId(id);
        return noteEleve;
    }

    private Utilisateur createUtilisateur(Integer id) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        return utilisateur;
    }

    @Test
    void testGetBonusMalus_NormalCase() {
        when(bonusMalusService.getBonusMalusSprint(anyInt(), anyInt())).thenReturn(bonusMalusList);
        List<BonusMalus> result = bonusMalusController.getBonusMalus(1, 1);
        assertEquals(1, result.size());
        assertEquals(bonusMalusList, result);
    }

    @Test
    void testGetBonusMalusById_NormalCase() {
        when(bonusMalusService.getBonusMalusById(anyInt())).thenReturn(bonusMalusList.get(0));
        BonusMalus result = bonusMalusController.getBonusMalusById(1);
        assertEquals(bonusMalusList.get(0), result);
    }

    @Test
    void testGetBonusMalusByNoteId_NormalCase() {
        when(bonusMalusService.getBonusMalusByNoteId(anyInt())).thenReturn(bonusMalusList.get(0));
        BonusMalus result = bonusMalusController.getBonusMalusByNoteId(1);
        assertEquals(bonusMalusList.get(0), result);
    }

    @Test
    void testAjouterBM_NormalCase() {
        BonusMalus bonusMalusToAdd = createBonusMalus(1, 2, createNoteEleve(1), createUtilisateur(1), "Commentaire");
        when(bonusMalusService.saveBonusMalus(any())).thenReturn(bonusMalusToAdd);
        ResponseEntity<BonusMalus> response = bonusMalusController.ajouterBM(bonusMalusToAdd);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bonusMalusToAdd, response.getBody());
    }

    @Test
    void testSupprimerBM_NormalCase() {
        int id = 1;
        doNothing().when(bonusMalusService).deleteBMbyId(id);
        ResponseEntity<String> response = bonusMalusController.supprimerBM(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("BM supprimé avec succès.", response.getBody());
    }

    @Test
    void testSupprimerBM_NotFoundCase() {
        int id = 1;
        doThrow(new RuntimeException()).when(bonusMalusService).deleteBMbyId(id);
        ResponseEntity<String> response = bonusMalusController.supprimerBM(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Erreur lors de la suppression du BM.", response.getBody());
    }

    @Test
    void getBonusMalusReturnsExpectedList() {
        List<BonusMalus> expectedList = List.of(new BonusMalus());
        when(bonusMalusService.getBonusMalusSprint(anyInt(), anyInt())).thenReturn(expectedList);

        List<BonusMalus> result = bonusMalusController.getBonusMalus(1, 1);

        assertEquals(expectedList, result);
    }

    @Test
    void getBonusMalusByIdReturnsExpectedBonusMalus() {
        BonusMalus expectedBonusMalus = new BonusMalus();
        when(bonusMalusService.getBonusMalusById(anyInt())).thenReturn(expectedBonusMalus);

        BonusMalus result = bonusMalusController.getBonusMalusById(1);

        assertEquals(expectedBonusMalus, result);
    }

    @Test
    void getBonusMalusByNoteIdReturnsExpectedBonusMalus() {
        BonusMalus expectedBonusMalus = new BonusMalus();
        when(bonusMalusService.getBonusMalusByNoteId(anyInt())).thenReturn(expectedBonusMalus);

        BonusMalus result = bonusMalusController.getBonusMalusByNoteId(1);

        assertEquals(expectedBonusMalus, result);
    }

    @Test
    void ajouterBMReturnsCreatedStatusAndExpectedBonusMalus() {
        BonusMalus expectedBonusMalus = new BonusMalus();
        when(bonusMalusService.saveBonusMalus(any())).thenReturn(expectedBonusMalus);

        ResponseEntity<BonusMalus> response = bonusMalusController.ajouterBM(expectedBonusMalus);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedBonusMalus, response.getBody());
    }

    @Test
    void supprimerBMReturnsOkStatusAndSuccessMessage() {
        doNothing().when(bonusMalusService).deleteBMbyId(anyInt());

        ResponseEntity<String> response = bonusMalusController.supprimerBM(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("BM supprimé avec succès.", response.getBody());
    }

    @Test
    void supprimerBMReturnsNotFoundStatusAndErrorMessageWhenExceptionThrown() {
        doThrow(new RuntimeException()).when(bonusMalusService).deleteBMbyId(anyInt());

        ResponseEntity<String> response = bonusMalusController.supprimerBM(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Erreur lors de la suppression du BM.", response.getBody());
    }

    @Test
    void modifierBonusMalusReturnsExpectedBonusMalus() {
        BonusMalus expectedBonusMalus = new BonusMalus();
        when(bonusMalusService.getBonusMalusById(anyInt())).thenReturn(expectedBonusMalus);
        when(bonusMalusService.saveBonusMalus(any())).thenReturn(expectedBonusMalus);

        ResponseEntity<BonusMalus> response = bonusMalusController.modifierBonusMalus(1, Map.of("valide", true));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBonusMalus, response.getBody());
    }

    @Test
    void modifierBonusMalusReturnsNotFoundStatusWhenBonusMalusNotFound() {
        when(bonusMalusService.getBonusMalusById(anyInt())).thenReturn(null);

        ResponseEntity<BonusMalus> response = bonusMalusController.modifierBonusMalus(1, Map.of("valide", true));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
