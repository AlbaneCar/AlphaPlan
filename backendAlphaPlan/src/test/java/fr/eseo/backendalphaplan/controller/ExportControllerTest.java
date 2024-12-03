package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.services.ExportNotesService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExportControllerTest {

    @InjectMocks
    private ExportController exportController;

    @Mock
    private ExportNotesService exportNotesService;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExportNotesSprintsCSV() throws IOException {
        doNothing().when(exportNotesService).getXSLX(response);

        ResponseEntity<String> result = exportController.exportNotesSprintsCSV(response);

        assertEquals("Exportation des notes des étudiants en format XLSX réussie.", result.getBody());
    }

    @Test
    void testExportNotesSprintsCSVWithException() throws IOException {
        doThrow(new IOException("Test exception")).when(exportNotesService).getXSLX(response);

        ResponseEntity<String> result = exportController.exportNotesSprintsCSV(response);

        assertEquals("Test exception", result.getBody());
    }
}