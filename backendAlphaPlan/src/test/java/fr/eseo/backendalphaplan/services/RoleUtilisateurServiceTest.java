package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Role;
import fr.eseo.backendalphaplan.model.RoleUtilisateur;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.RoleRepository;
import fr.eseo.backendalphaplan.repository.RoleUtilisateurRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RoleUtilisateurServiceTest {

    @InjectMocks
    private RoleUtilisateurService roleUtilisateurService;

    @Mock
    private RoleUtilisateurRepository roleUtilisateurRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoleUtilisateur() {
        RoleUtilisateur roleUtilisateur = new RoleUtilisateur();
        when(roleUtilisateurRepository.findAllWithUtilisateur()).thenReturn(Collections.singletonList(roleUtilisateur));

        List<RoleUtilisateur> result = roleUtilisateurService.getRoleUtilisateur();

        assertEquals(1, result.size());
        assertEquals(roleUtilisateur, result.get(0));
    }

    @Test
    void testDeleteRoleUtilisateur() {
        RoleUtilisateur roleUtilisateur = new RoleUtilisateur();
        
        roleUtilisateurService.deleteRoleUtilisateur(roleUtilisateur);

        verify(roleUtilisateurRepository, times(1)).delete(roleUtilisateur);
    }

    @Test
    void testSaveRoleUtilisateur() {
        RoleUtilisateur roleUtilisateur = new RoleUtilisateur();
        
        roleUtilisateurService.saveRoleUtilisateur(roleUtilisateur);

        verify(roleUtilisateurRepository, times(1)).save(roleUtilisateur);
    }

    @Test
    void testExceptionInvalidUser() {
        when(utilisateurRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(new Role()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roleUtilisateurService.setRoleUtilisateur(1, 1);
        });

        assertEquals("Utilisateur non trouvé avec l'ID : 1", exception.getMessage());
    }

    @Test
    void testExceptionInvalidRole() {
        when(utilisateurRepository.findById(anyInt())).thenReturn(Optional.of(new Utilisateur()));
        when(roleRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roleUtilisateurService.setRoleUtilisateur(1, 1);
        });

        assertEquals("Rôle non trouvé avec l'ID : 1", exception.getMessage());
    }
}
