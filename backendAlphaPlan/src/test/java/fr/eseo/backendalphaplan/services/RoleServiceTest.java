package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Role;
import fr.eseo.backendalphaplan.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoles() {
        Role role = new Role();
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(role));

        List<Role> result = roleService.getRoles();

        assertEquals(1, result.size());
    }

    @Test
    void testGetRolesByStudents() {
        Role role = new Role();
        when(roleRepository.findRoles(anyList())).thenReturn(Collections.singletonList(role));

        List<Role> result = roleService.getRolesByStudents(Collections.singletonList(1));

        assertEquals(1, result.size());
    }
}