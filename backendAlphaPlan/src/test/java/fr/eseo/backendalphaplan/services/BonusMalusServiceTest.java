package fr.eseo.backendalphaplan.services;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.eseo.backendalphaplan.model.BonusMalus;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.NoteEleve;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.BonusMalusRepository;
import fr.eseo.backendalphaplan.services.BonusMalusService;

class BonusMalusServiceTest {

    @Mock
    private BonusMalusRepository bonusMalusRepository;

    @InjectMocks
    private BonusMalusService bonusMalusService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveBonusMalus() {
        BonusMalus bonusMalus = createBonusMalus(1, 2, createNoteEleve(1), createUtilisateur(1), "Commentaire");
        
        when(bonusMalusRepository.save(any(BonusMalus.class))).thenReturn(bonusMalus);

        BonusMalus result = bonusMalusService.saveBonusMalus(bonusMalus);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(2, result.getValeur());
        assertEquals("Commentaire", result.getCommentaire());
    }
    
    @Test
    void testGetBonusMalusSprint_1() {
        List<BonusMalus> BMsprint1 = new ArrayList<>();
        BonusMalus bonusMalus1 = createBonusMalus(1, 2, createNoteEleveWithSprint(1, createSprint(1)), createUtilisateurWithTeam(1, createTeam(1)), "Commentaire");
        
        BMsprint1.add(bonusMalus1);

        when(bonusMalusRepository.getBonusMalusSprint(1, 1)).thenReturn(BMsprint1);

        List<BonusMalus> resultList = bonusMalusService.getBonusMalusSprint(1, 1);

        assertEquals(BMsprint1.size(), resultList.size());
    }
    
    @Test
    void testGetBonusMalusSprint_2() {
        List<BonusMalus> BMsprint1 = new ArrayList<>();
        BonusMalus bonusMalus1 = createBonusMalus(1, 2, createNoteEleveWithSprint(1, createSprint(1)), createUtilisateurWithTeam(1, createTeam(1)), "Commentaire");
        BonusMalus bonusMalus2 = createBonusMalus(1, 2, createNoteEleveWithSprint(1, createSprint(1)), createUtilisateurWithTeam(1, createTeam(1)), "Commentaire");
        
        BMsprint1.add(bonusMalus1);
        BMsprint1.add(bonusMalus2);

        when(bonusMalusRepository.getBonusMalusSprint(1, 1)).thenReturn(BMsprint1);

        List<BonusMalus> resultList = bonusMalusService.getBonusMalusSprint(1, 1);

        assertEquals(BMsprint1.size(), resultList.size());
    }

    @Test
    void testGetBonusMalusById() {
        BonusMalus bonusMalus = createBonusMalus(1, 3, createNoteEleve(1), createUtilisateur(1), "Commentaire");
        when(bonusMalusRepository.findById(1)).thenReturn(Optional.of(bonusMalus));

        Optional<BonusMalus> result = Optional.ofNullable(bonusMalusService.getBonusMalusById(1));

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals(3, result.get().getValeur());
        assertEquals("Commentaire", result.get().getCommentaire());
    }

    @Test
    void testGetBonusMalusByNoteId() {
        BonusMalus bonusMalus = createBonusMalus(3, 0, createNoteEleve(2), createUtilisateur(1), "Commentaire");
        when(bonusMalusRepository.findByNoteId(2)).thenReturn(bonusMalus);

        BonusMalus result = bonusMalusService.getBonusMalusByNoteId(2);

        assertNotNull(result);
        assertEquals(3, result.getId());
        assertEquals(0, result.getValeur());
        assertEquals("Commentaire", result.getCommentaire());
    }

    @Test
    void testDeleteBMbyId() {
        bonusMalusService.deleteBMbyId(1);

        verify(bonusMalusRepository, times(1)).deleteById(1);
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
    
    private NoteEleve createNoteEleveWithSprint(Integer id, Sprint sprint) {
        NoteEleve noteEleve = new NoteEleve();
        noteEleve.setId(id);
        noteEleve.setSprint(sprint);
        return noteEleve;
    }

    private Utilisateur createUtilisateur(Integer id) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        return utilisateur;
    }
    
    private Utilisateur createUtilisateurWithTeam(Integer id, Equipe id2) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        utilisateur.setEquipe(id2);
        return utilisateur;
    }
    
    private Sprint createSprint(Integer id) {
    	Sprint sprint = new Sprint();
    	sprint.setId(id);
        return sprint;
    }
    
    private Equipe createTeam(Integer id) {
    	Equipe equipe = new Equipe();
    	equipe.setId(id);
        return equipe;
    }
}
