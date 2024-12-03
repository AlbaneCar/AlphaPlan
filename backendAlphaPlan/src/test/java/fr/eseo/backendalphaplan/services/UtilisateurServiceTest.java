package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.NoteAnterieure;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;
import fr.eseo.backendalphaplan.repository.NoteAnterieureRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UtilisateurServiceTest {

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private NoteAnterieureRepository noteAnterieureRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUtilisateurById() {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurRepository.findById(anyInt())).thenReturn(Optional.of(utilisateur));

        var result = utilisateurService.getUtilisateurById(1);

        assertEquals(utilisateur, result);
    }

    @Test
    void testGetUtilisateurByEmail() {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurRepository.findByEmail(anyString())).thenReturn(utilisateur);

        var result = utilisateurService.getUtilisateurByEmail("test@test.com");

        assertEquals(utilisateur, result);
    }

    @Test
    void testGetUtilisateurs() {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurRepository.findAll()).thenReturn(Collections.singletonList(utilisateur));

        List<Utilisateur> result = utilisateurService.getUtilisateurs();

        assertEquals(1, result.size());
    }

    @Test
    void testFindByNomAndPrenom() {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurRepository.findNomAndPrenom(anyString(), anyString())).thenReturn(utilisateur);

        var result = utilisateurService.findByNomAndPrenom("John", "Doe");

        assertEquals(utilisateur, result);
    }

    @Test
    void testDeleteUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        doNothing().when(utilisateurRepository).delete(any(Utilisateur.class));

        utilisateurService.deleteUtilisateur(utilisateur);

        verify(utilisateurRepository, times(1)).delete(utilisateur);
    }

    @Test
    void testGetUtilisateursEncadrant() {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurRepository.findEncadrant(any(TypeUtilisateur.class))).thenReturn(Collections.singletonList(utilisateur));

        List<Utilisateur> result = utilisateurService.getUtilisateursEncadrant();

        assertEquals(1, result.size());
    }

    @Test
    void testGetEleves() {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurRepository.findEleve(any(TypeUtilisateur.class), any(TypeUtilisateur.class))).thenReturn(Collections.singletonList(utilisateur));

        List<Utilisateur> result = utilisateurService.getEleves();

        assertEquals(1, result.size());
    }

    @Test
    void testSaveUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        var result = utilisateurService.saveUtilisateur(utilisateur);

        assertEquals(utilisateur, result);
    }

    @Test
    void testGetUtilisateursByEquipe() {
        Utilisateur utilisateur = new Utilisateur();
        Equipe equipe = new Equipe();
        when(utilisateurRepository.findByEquipe(any(Equipe.class))).thenReturn(Collections.singletonList(utilisateur));

        List<Utilisateur> result = utilisateurService.getUtilisateursByEquipe(equipe);

        assertEquals(1, result.size());
    }

    @Test
    void testGetStudents() {
        Utilisateur utilisateur = new Utilisateur();
        Page<Utilisateur> page = new PageImpl<>(Collections.singletonList(utilisateur));
        when(utilisateurRepository.findAllE3EOrBachelorUsers(any(Pageable.class))).thenReturn(page);

        Page<Utilisateur> result = utilisateurService.getStudents(Pageable.unpaged());

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testSaveNoteAnterieure() {
        NoteAnterieure noteAnterieure = new NoteAnterieure();
        when(noteAnterieureRepository.save(any(NoteAnterieure.class))).thenReturn(noteAnterieure);

        utilisateurService.saveNoteAnterieure(noteAnterieure);

        verify(noteAnterieureRepository, times(1)).save(noteAnterieure);
    }



    @Test
    void testDeleteUserById() {
        doNothing().when(utilisateurRepository).deleteById(anyInt());

        utilisateurService.deleteUserById(1);

        verify(utilisateurRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetEncadrants() {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurRepository.findEncadrants()).thenReturn(Collections.singletonList(utilisateur));

        List<Utilisateur> result = utilisateurService.getEncadrants();

        assertEquals(1, result.size());
    }
}