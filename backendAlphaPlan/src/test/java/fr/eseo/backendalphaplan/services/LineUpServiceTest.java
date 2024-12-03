package fr.eseo.backendalphaplan.services;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.ValidationLineUp;
import fr.eseo.backendalphaplan.model.enums.StatusLineUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.eseo.backendalphaplan.controller.LineUpController;
import fr.eseo.backendalphaplan.model.LineUp;
import fr.eseo.backendalphaplan.repository.LineUpRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import fr.eseo.backendalphaplan.repository.ValidationLineUpRepository;

class LineUpServiceTest {

    @InjectMocks
    private LineUpService lineUpService;

    @Mock
    private LineUpRepository lineUpRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private ValidationLineUpRepository validationLineUpRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLineUp_Failure_AuteurNotFound() {
        LineUpController.LineUpRequest request = new LineUpController.LineUpRequest();
        request.setAuteur(1);
        request.setProposition(2);

        when(utilisateurRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lineUpService.createLineUp(request));
    }

    @Test
    void testGetAllLineUps_Success() {
        LineUp lineUp = new LineUp();
        when(lineUpRepository.findAll()).thenReturn(Collections.singletonList(lineUp));

        assertEquals(1, lineUpService.getAll().size());
    }

    @Test
    void testGetAllLineUps_Empty() {
        when(lineUpRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(lineUpService.getAll().isEmpty());
    }



    @Test
    //antoine
    void testCreateValidationLineUp_UserWithoutTeam_ThrowsException() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setEquipe(null);

        LineUp lineUp = new LineUp();


        Method method = LineUpService.class.getDeclaredMethod("createValidationLineUp", Utilisateur.class, LineUp.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(lineUpService, utilisateur, lineUp);
        });

        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
        assertEquals("Il n'y a pas d'équipe associé à l'utilisateur avec l'id : 1", exception.getCause().getMessage());
    }

    @Test
    //antoine
    void testCreateValidationLineUp_UserWithTeam_SavesValidationLineUp() throws Exception {
        Equipe equipe = new Equipe();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setEquipe(equipe);

        Utilisateur membre1 = new Utilisateur();
        membre1.setId(1);
        Utilisateur membre2 = new Utilisateur();
        membre2.setId(2);

        List<Utilisateur> membres = Arrays.asList(membre1, membre2);

        when(utilisateurRepository.findByEquipe(equipe)).thenReturn(membres);

        LineUp lineUp = new LineUp();
        lineUp.setAuteur(membre1);


        Method method = LineUpService.class.getDeclaredMethod("createValidationLineUp", Utilisateur.class, LineUp.class);
        method.setAccessible(true);
        method.invoke(lineUpService, utilisateur, lineUp);


        verify(validationLineUpRepository, times(2)).save(any(ValidationLineUp.class));
        verify(validationLineUpRepository).save(argThat(validationLineUp ->
                validationLineUp.getUtilisateur().equals(membre1) &&
                        validationLineUp.getStatus().equals(StatusLineUp.ACCEPTE)
        ));
        verify(validationLineUpRepository).save(argThat(validationLineUp ->
                validationLineUp.getUtilisateur().equals(membre2) &&
                        validationLineUp.getStatus().equals(StatusLineUp.ATTENTE)
        ));
    }


    @Test
    //antoine
    void testCreateValidationLineUp_UserWithTeamAndIsNotAuthor_SavesValidationLineUp() throws Exception {
        Equipe equipe = new Equipe();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setEquipe(equipe);

        Utilisateur membre1 = new Utilisateur();
        membre1.setId(2);
        Utilisateur membre2 = new Utilisateur();
        membre2.setId(3);

        List<Utilisateur> membres = Arrays.asList(membre1, membre2);


        when(utilisateurRepository.findByEquipe(equipe)).thenReturn(membres);

        Utilisateur auteur = new Utilisateur();
        auteur.setId(4);
        LineUp lineUp = new LineUp();
        lineUp.setAuteur(auteur);


        Method method = LineUpService.class.getDeclaredMethod("createValidationLineUp", Utilisateur.class, LineUp.class);
        method.setAccessible(true);
        method.invoke(lineUpService, utilisateur, lineUp);


        verify(validationLineUpRepository, times(2)).save(any(ValidationLineUp.class));
        verify(validationLineUpRepository).save(argThat(validationLineUp ->
                validationLineUp.getUtilisateur().equals(membre1) &&
                        validationLineUp.getStatus().equals(StatusLineUp.ATTENTE)
        ));
        verify(validationLineUpRepository).save(argThat(validationLineUp ->
                validationLineUp.getUtilisateur().equals(membre2) &&
                        validationLineUp.getStatus().equals(StatusLineUp.ATTENTE)
        ));
    }

    @Test
    //antoine
    void testUpdateLineUp_NullArguments_ThrowsException() {
        LineUpController.ValidationRequest req = new LineUpController.ValidationRequest();
        req.setProposition(null);
        req.setReponse(true);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.updateLineUp(req);
        });

        assertEquals("Un des arguments de la requête est nul !", exception.getMessage());

        req.setProposition(1);
        req.setReponse(null);

        exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.updateLineUp(req);
        });

        assertEquals("Un des arguments de la requête est nul !", exception.getMessage());
    }

    @Test
    //antoine
    void testUpdateLineUp_LineUpNotFound_ThrowsException() {
        LineUpController.ValidationRequest req = new LineUpController.ValidationRequest();
        req.setProposition(1);
        req.setReponse(true);

        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.updateLineUp(req);
        });

        assertEquals("Aucune line-up n'est disponible pour cet id !", exception.getMessage());
    }

    @Test
    //antoine
    void testUpdateLineUp_ResponseFalse_DeletesLineUp() {
        LineUpController.ValidationRequest req = new LineUpController.ValidationRequest();
        req.setProposition(1);
        req.setReponse(false);

        LineUp lineUp = new LineUp();
        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.of(lineUp));


        String result = lineUpService.updateLineUp(req);

        verify(lineUpRepository).delete(lineUp);
        assertEquals("La proposition de line-up a bien été rejetée !", result);
    }


    @Test
    //antoine
    void testUpdateLineUp_ResponseTrue_SuccessfulUpdate() {
        LineUpController.ValidationRequest req = new LineUpController.ValidationRequest();
        req.setProposition(1);
        req.setReponse(true);

        LineUp lineUp = new LineUp();
        Utilisateur auteur = new Utilisateur();
        Utilisateur proposition = new Utilisateur();
        Equipe equipeAuteur = new Equipe();
        Equipe equipeProposition = new Equipe();
        auteur.setEquipe(equipeAuteur);
        proposition.setEquipe(equipeProposition);

        lineUp.setAuteur(auteur);
        lineUp.setProposition(proposition);

        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.of(lineUp));

        String result = lineUpService.updateLineUp(req);

        verify(utilisateurRepository, times(2)).save(auteur);
        verify(utilisateurRepository, times(2)).save(proposition);
        assertEquals("La proposition a bien été appliquée !", result);
        assertEquals(equipeProposition, auteur.getEquipe());
        assertEquals(equipeAuteur, proposition.getEquipe());
    }



    @Test
    //antoine
    void testUpdateLineUp_UserWithoutTeam_ThrowsException() {
        LineUpController.ValidationRequest req = new LineUpController.ValidationRequest();
        req.setProposition(1);
        req.setReponse(true);

        LineUp lineUp = new LineUp();
        Utilisateur auteur = new Utilisateur();
        Utilisateur proposition = new Utilisateur();
        Equipe equipeProposition = new Equipe();
        auteur.setEquipe(null);
        proposition.setEquipe(equipeProposition);

        lineUp.setAuteur(auteur);
        lineUp.setProposition(proposition);

        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.of(lineUp));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.updateLineUp(req);
        });

        assertEquals("Un des élèves à échanger n'est pas dans une équipe", exception.getMessage());
    }

    @Test
    //antoine
    void testUpdateLineUp_PropositionUserWithoutTeam_ThrowsException() {
        LineUpController.ValidationRequest req = new LineUpController.ValidationRequest();
        req.setProposition(1);
        req.setReponse(true);

        LineUp lineUp = new LineUp();
        Utilisateur auteur = new Utilisateur();
        Utilisateur proposition = new Utilisateur();
        Equipe equipeAuteur = new Equipe();
        auteur.setEquipe(equipeAuteur);
        proposition.setEquipe(null);

        lineUp.setAuteur(auteur);
        lineUp.setProposition(proposition);

        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.of(lineUp));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.updateLineUp(req);
        });

        assertEquals("Un des élèves à échanger n'est pas dans une équipe", exception.getMessage());
    }


    @Test
    //antoine
    void testGetLineUpByAuteur_NullAuteurId_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.getLineUpByAuteur(null);
        });

        assertEquals("Aucun argument renseigné !", exception.getMessage());
    }

    @Test
    //antoine
    void testGetLineUpByAuteur_UserNotFound_ThrowsException() {
        // Arrange
        Integer auteurId = 1;
        when(utilisateurRepository.findById(auteurId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.getLineUpByAuteur(auteurId);
        });

        assertEquals("Aucun utilisateur avec cet id !", exception.getMessage());
    }

    @Test
    //antoine
    void testGetLineUpByAuteur_Success() {
        Integer auteurId = 1;
        Utilisateur auteur = new Utilisateur();
        auteur.setId(auteurId);

        LineUp lineUp1 = new LineUp();
        LineUp lineUp2 = new LineUp();
        List<LineUp> lineUps = Arrays.asList(lineUp1, lineUp2);

        when(utilisateurRepository.findById(auteurId)).thenReturn(Optional.of(auteur));
        when(lineUpRepository.findByAuteur(auteur)).thenReturn(lineUps);

        List<LineUp> result = lineUpService.getLineUpByAuteur(auteurId);

        assertEquals(lineUps, result);
        verify(utilisateurRepository).findById(auteurId);
        verify(lineUpRepository).findByAuteur(auteur);
    }


    @Test
    //antoine
    void testDeleteLineUp_NullArguments_ThrowsException() {
        LineUpController.LineUpRequest req = new LineUpController.LineUpRequest();
        req.setAuteur(null);
        req.setProposition(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.deleteLineUp(req);
        });

        assertEquals("Au moins un des arguments est absent !", exception.getMessage());

        req.setAuteur(1);
        req.setProposition(null);

        exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.deleteLineUp(req);
        });

        assertEquals("Au moins un des arguments est absent !", exception.getMessage());
    }

    @Test
    //antoine
    void testDeleteLineUp_UserOrLineUpNotFound_ThrowsException() {
        LineUpController.LineUpRequest req = new LineUpController.LineUpRequest();
        req.setAuteur(1);
        req.setProposition(1);

        when(utilisateurRepository.findById(req.getAuteur())).thenReturn(Optional.empty());
        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.deleteLineUp(req);
        });

        assertEquals("Au moins une des personnes rensignées n'existe pas !", exception.getMessage());

        Utilisateur auteur = new Utilisateur();
        auteur.setId(1);
        when(utilisateurRepository.findById(req.getAuteur())).thenReturn(Optional.of(auteur));
        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.empty());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.deleteLineUp(req);
        });

        assertEquals("Au moins une des personnes rensignées n'existe pas !", exception.getMessage());

        when(utilisateurRepository.findById(req.getAuteur())).thenReturn(Optional.empty());
        LineUp lineUp = new LineUp();
        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.of(lineUp));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.deleteLineUp(req);
        });

        assertEquals("Au moins une des personnes rensignées n'existe pas !", exception.getMessage());
    }

    @Test
    //antoine
    void testDeleteLineUp_Success() {
        LineUpController.LineUpRequest req = new LineUpController.LineUpRequest();
        req.setAuteur(1);
        req.setProposition(1);

        Utilisateur auteur = new Utilisateur();
        auteur.setId(1);

        Utilisateur autreUtilisateur = new Utilisateur();
        autreUtilisateur.setId(2);

        LineUp lineUp = new LineUp();
        lineUp.setAuteur(auteur);

        when(utilisateurRepository.findById(req.getAuteur())).thenReturn(Optional.of(auteur));
        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.of(lineUp));


        String result = lineUpService.deleteLineUp(req);

        verify(lineUpRepository).delete(lineUp);
        assertEquals("Proposition supprimée avec succès !", result);
    }


    @Test
    //antoine
    void testDeleteLineUp_NotAuthor_ThrowsException() {
        LineUpController.LineUpRequest req = new LineUpController.LineUpRequest();
        req.setAuteur(1);
        req.setProposition(1);

        Utilisateur auteur = new Utilisateur();
        auteur.setId(1);

        Utilisateur autreAuteur = new Utilisateur();
        autreAuteur.setId(2);

        LineUp lineUp = new LineUp();
        lineUp.setAuteur(autreAuteur);

        when(utilisateurRepository.findById(req.getAuteur())).thenReturn(Optional.of(auteur));
        when(lineUpRepository.findById(req.getProposition())).thenReturn(Optional.of(lineUp));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            lineUpService.deleteLineUp(req);
        });

        assertEquals("Vous ne pouvez pas supprimer une proposition dont vous n'êtes pas l'auteur !", exception.getMessage());
    }
}
