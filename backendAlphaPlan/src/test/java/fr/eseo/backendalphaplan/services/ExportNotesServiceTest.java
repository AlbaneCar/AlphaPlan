package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class ExportNotesServiceTest {

    @InjectMocks
    private ExportNotesService exportNotesService;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private NoteEleveRepository noteEleveRepository;

    @Mock
    private NoteEquipeRepository noteEquipeRepository;

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetXSLXException() {
        when(equipeRepository.findAll()).thenReturn(Collections.singletonList(new Equipe()));
        when(sprintRepository.findAll()).thenReturn(Collections.singletonList(new Sprint()));

        assertThrows(NullPointerException.class, () -> {
            exportNotesService.getXSLX(response);
        });

        verify(equipeRepository, times(1)).findAll();
        verify(sprintRepository, times(1)).findAll();
    }

    @Test
    void testGetXSLXWithStudent() throws IOException {
        Equipe equipe = new Equipe();
        equipe.setNom("Test Team");  // Set the team name

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("Test Student");  // Set the student name
        utilisateur.setEquipe(equipe);  // Add the student to the team

        when(equipeRepository.findAll()).thenReturn(Collections.singletonList(equipe));
        when(sprintRepository.findAll()).thenReturn(Collections.singletonList(new Sprint()));
        when(utilisateurRepository.findByEquipe(equipe)).thenReturn(Collections.singletonList(utilisateur));

        // Mock the OutputStream of the response
        OutputStream outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        });

        exportNotesService.getXSLX(response);

        verify(equipeRepository, times(1)).findAll();
        verify(sprintRepository, times(1)).findAll();
        verify(utilisateurRepository, times(1)).findByEquipe(equipe);
    }
}