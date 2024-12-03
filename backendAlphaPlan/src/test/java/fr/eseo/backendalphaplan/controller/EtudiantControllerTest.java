package fr.eseo.backendalphaplan.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;
import fr.eseo.backendalphaplan.services.UtilisateurService;

class EtudiantControllerTest {

    private EtudiantController etudiantController;
    private UtilisateurService utilisateurServiceMock;
    private List<Utilisateur> utilisateurs;
    private List<Utilisateur> students;
    private List<Utilisateur> teachers;

    @BeforeEach
    void setUp() {
        utilisateurServiceMock = mock(UtilisateurService.class);
        etudiantController = new EtudiantController(utilisateurServiceMock);
        utilisateurs = new ArrayList<>();
        students = new ArrayList<>();
        teachers = new ArrayList<>();
        Utilisateur user1 = new Utilisateur();
        user1.setId(1);
        user1.setNom("Doe");
        user1.setPrenom("John");
        user1.setTypeUtilisateur(TypeUtilisateur.ENCADRANT);
        utilisateurs.add(user1);
        Utilisateur user2 = new Utilisateur();
        user2.setId(2);
        user2.setNom("Hola");
        user2.setPrenom("Marc");
        user2.setTypeUtilisateur(TypeUtilisateur.E3E);
        utilisateurs.add(user2);
        Utilisateur user3 = new Utilisateur();
        user3.setId(3);
        user3.setNom("David");
        user3.setPrenom("Tim");
        user3.setTypeUtilisateur(TypeUtilisateur.BACHELOR);
        utilisateurs.add(user3);
        students.add(user2);
        students.add(user3);
        teachers.add(user1);
    }

    @Test
    void testGetStudents() {
    	//configuration du mock
        when(utilisateurServiceMock.getUtilisateurs()).thenReturn(utilisateurs);

        //appel de la méthode à tester
        List<Utilisateur> result = etudiantController.getStudents();

        //vérifications des résultats
        assertEquals(3, result.size());
        assertEquals("Doe", result.get(0).getNom());
        assertEquals("Marc", result.get(1).getPrenom());
        assertEquals(TypeUtilisateur.BACHELOR, result.get(2).getTypeUtilisateur());
        
        //vérifications des interactions
        verify(utilisateurServiceMock, times(1)).getUtilisateurs();
        verifyNoMoreInteractions(utilisateurServiceMock);
    }

    @Test
    void testGetUserById() {
        when(utilisateurServiceMock.getUtilisateurById(1)).thenReturn(utilisateurs.get(0));
        Utilisateur result = etudiantController.getUserById(1);
        assertEquals(utilisateurs.get(0), result);
        
        verify(utilisateurServiceMock, times(1)).getUtilisateurById(1);
        verifyNoMoreInteractions(utilisateurServiceMock);
    }
    
    @Test
    void testGetEtudiants() {
        when(utilisateurServiceMock.getEleves()).thenReturn(students);

        List<Utilisateur> result = etudiantController.getEtudiants();

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getId());
        assertEquals(3, result.get(1).getId());
        
        verify(utilisateurServiceMock, times(1)).getEleves();
        verifyNoMoreInteractions(utilisateurServiceMock);
    }

    @Test
    void testGetTeachers() {
        when(utilisateurServiceMock.getUtilisateursEncadrant()).thenReturn(teachers);

        List<Utilisateur> result = etudiantController.getTeachers();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(TypeUtilisateur.ENCADRANT, result.get(0).getTypeUtilisateur());
        
        verify(utilisateurServiceMock, times(1)).getUtilisateursEncadrant();
        verifyNoMoreInteractions(utilisateurServiceMock);
    }
    
    @Test
    void testAddStudent() {
        Utilisateur userToAdd = new Utilisateur();
        userToAdd.setId(4);
        userToAdd.setNom("Smith");
        userToAdd.setPrenom("Alice");
        userToAdd.setTypeUtilisateur(TypeUtilisateur.E3E);

        when(utilisateurServiceMock.saveUtilisateur(userToAdd)).thenReturn(userToAdd);

        ResponseEntity<Utilisateur> response = etudiantController.addStudent(userToAdd);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userToAdd, response.getBody());
        
        verify(utilisateurServiceMock, times(1)).saveUtilisateur(userToAdd);
    }

    @Test
    void testDeleteUser() {
    	//définition de l'utilisateur à supprimer
    	int userIdToDelete = 4;

    	//configuration du mock pour tester le controller
        doNothing().when(utilisateurServiceMock).deleteUserById(userIdToDelete);

        //réponse de la requête
        ResponseEntity<String> response = etudiantController.deleteUser(userIdToDelete);

        //vérification de la réponse
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("L'utilisateur a été supprimé avec succès.", response.getBody());
        
        //vérification des interactions
        verify(utilisateurServiceMock, times(1)).deleteUserById(userIdToDelete);
        verifyNoMoreInteractions(utilisateurServiceMock);
    }

    @Test
    void testDeleteImpossible() {
        int userIdToDelete = 5;

        doThrow(EmptyResultDataAccessException.class).when(utilisateurServiceMock).deleteUserById(userIdToDelete);

        ResponseEntity<String> response = etudiantController.deleteUser(userIdToDelete);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Erreur lors de la suppression de l'utilisateur.", response.getBody());
        
        verify(utilisateurServiceMock, times(1)).deleteUserById(userIdToDelete);
        verifyNoMoreInteractions(utilisateurServiceMock);
    }

    @Test
    void testAddExisting() {
        Utilisateur userToAdd = new Utilisateur();
        userToAdd.setId(4);
        userToAdd.setNom("Smith");
        userToAdd.setPrenom("Alice");
        userToAdd.setTypeUtilisateur(TypeUtilisateur.E3E);

        when(utilisateurServiceMock.getUtilisateurById(userToAdd.getId())).thenReturn(userToAdd);
        ResponseEntity<Utilisateur> response = etudiantController.addStudent(userToAdd);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(userToAdd, response.getBody());

        verify(utilisateurServiceMock, never()).saveUtilisateur(userToAdd);
    }

}
