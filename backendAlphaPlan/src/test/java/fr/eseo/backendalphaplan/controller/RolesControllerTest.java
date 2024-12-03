package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.Role;
import fr.eseo.backendalphaplan.model.RoleUtilisateur;
import fr.eseo.backendalphaplan.services.RoleService;
import fr.eseo.backendalphaplan.services.RoleUtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RolesControllerTest {

    @InjectMocks
    private RolesController rolesController;

    @Mock
    private RoleService roleService;

    @Mock
    private RoleUtilisateurService roleUtilisateurService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoles() {
        List<Role> expected = new ArrayList<>();
        when(roleService.getRoles()).thenReturn(expected);

        List<Role> result = rolesController.getRoles();

        assertEquals(expected, result);
        verify(roleService, times(1)).getRoles();
    }

    @Test
    void testGetRolesByStudents() {
        List<Role> expected = new ArrayList<>();
        when(roleService.getRolesByStudents(anyList())).thenReturn(expected);

        List<Role> result = rolesController.getRolesByStudents(new ArrayList<>());

        assertEquals(expected, result);
        verify(roleService, times(1)).getRolesByStudents(anyList());
    }

    @Test
    void testGetRoleUtilisateur() {
        List<RoleUtilisateur> expected = new ArrayList<>();
        when(roleUtilisateurService.getRoleUtilisateur()).thenReturn(expected);

        List<RoleUtilisateur> result = rolesController.getRoleUtilisateur();

        assertEquals(expected, result);
        verify(roleUtilisateurService, times(1)).getRoleUtilisateur();
    }

    @Test
    void testSetRoleUtilisateur() {
        // Prepare test data
        Integer userId = 1;
        Integer roleId = 1;
        List<RoleUtilisateur> expected = Arrays.asList(new RoleUtilisateur(), new RoleUtilisateur());

        // Mock the behavior of roleUtilisateurService
        when(roleUtilisateurService.setRoleUtilisateur(anyInt(), anyInt())).thenReturn(expected);

        // Call the method
        List<RoleUtilisateur> result = rolesController.setRoleUtilisateur(userId, roleId);

        // Verify the results
        assertEquals(expected, result);
    }
}