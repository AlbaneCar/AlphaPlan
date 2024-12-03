package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.*;
import fr.eseo.backendalphaplan.model.enums.Genre;
import fr.eseo.backendalphaplan.services.EquipeService;
import fr.eseo.backendalphaplan.services.LectureExcelService;
import fr.eseo.backendalphaplan.services.NoteAnterieureService;
import fr.eseo.backendalphaplan.services.UtilisateurService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UtilisateurControllerTest {

    @InjectMocks
    private UtilisateurController utilisateurController;

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private EquipeService equipeService;

    @Mock
    private LectureExcelService lectureExcelService;

    @Mock
    private NoteAnterieureService noteAnterieureService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<Utilisateur> expected = new ArrayList<>();
        when(utilisateurService.getUtilisateurs()).thenReturn(expected);

        Iterable<Utilisateur> result = utilisateurController.getAllUsers();

        assertEquals(expected, result);
        verify(utilisateurService, times(1)).getUtilisateurs();
    }

    @Test
    void testGetAllStudents() {
        Page<Utilisateur> expected = mock(Page.class);
        when(utilisateurService.getStudents(any(Pageable.class))).thenReturn(expected);

        Page<Utilisateur> result = utilisateurController.getAllStudents(mock(Pageable.class));

        assertEquals(expected, result);
        verify(utilisateurService, times(1)).getStudents(any(Pageable.class));
    }

    @Test
    void testGetUserById() {
        Utilisateur expected = new Utilisateur();
        when(utilisateurService.getUtilisateurById(anyInt())).thenReturn(expected);

        Utilisateur result = utilisateurController.getUserById(anyInt());

        assertEquals(expected, result);
        verify(utilisateurService, times(1)).getUtilisateurById(anyInt());
    }

    @Test
    void testGetNotesAnterieures() {
        List<NoteAnterieure> expected = new ArrayList<>();
        when(noteAnterieureService.getAllNotesAnterieures(anyInt())).thenReturn(expected);

        List<NoteAnterieure> result = utilisateurController.getNotesAnterieures(anyInt());

        assertEquals(expected, result);
        verify(noteAnterieureService, times(1)).getAllNotesAnterieures(anyInt());
    }

    @Test
    void testGetUsersByEquipeId() {
        List<Utilisateur> expected = new ArrayList<>();
        Equipe equipe = new Equipe();
        when(equipeService.getEquipeById(anyInt())).thenReturn(equipe);
        when(utilisateurService.getUtilisateursByEquipe(equipe)).thenReturn(expected);

        Iterable<Utilisateur> result = utilisateurController.getUsersByEquipeId(anyInt());

        assertEquals(expected, result);
        verify(utilisateurService, times(1)).getUtilisateursByEquipe(equipe);
    }

    @Test
    void testGetEncadrants() {
        List<Utilisateur> expected = new ArrayList<>();
        when(utilisateurService.getEncadrants()).thenReturn(expected);

        List<Utilisateur> result = utilisateurController.getEncadrants();

        assertEquals(expected, result);
        verify(utilisateurService, times(1)).getEncadrants();
    }

    @Test
    void testAddPerson() {
        // Prepare test data
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("nom", "Doe");
        requestBody.put("prenom", "John");
        requestBody.put("email", "john.doe@example.com");
        requestBody.put("password", "password");
        requestBody.put("genre", "HOMME");
        requestBody.put("equipe_id", "1");

        Utilisateur expectedUser = new Utilisateur();
        expectedUser.setNom("Doe");
        expectedUser.setPrenom("John");
        expectedUser.setEmail("john.doe@example.com");
        expectedUser.setMotDePasse("password");
        expectedUser.setGenre(Genre.HOMME);

        Equipe equipe = new Equipe();
        equipe.setId(1);
        expectedUser.setEquipe(equipe);

        // Mock the behavior of utilisateurService and equipeService
        when(utilisateurService.saveUtilisateur(expectedUser)).thenReturn(expectedUser);
        when(equipeService.getEquipeById(1)).thenReturn(equipe);

        // Call the method under test
        Utilisateur result = utilisateurController.addPerson(requestBody);

        // Verify the results
        assertEquals(expectedUser, result);

        // Verify the interactions with the mock
        verify(utilisateurService, times(1)).saveUtilisateur(expectedUser);
        verify(equipeService, times(1)).getEquipeById(1);
    }

    @Test
    void shouldReturnOLPLUsers() {
        List<Utilisateur> expected = new ArrayList<>();
        when(utilisateurService.getOLPL()).thenReturn(expected);

        List<Utilisateur> result = utilisateurController.getOLPL();

        assertEquals(expected, result);
        verify(utilisateurService, times(1)).getOLPL();
    }

    @Test
    void shouldReturnEmptyListWhenEquipeDoesNotExist() {
        when(equipeService.getEquipeById(anyInt())).thenReturn(null);

        Iterable<Utilisateur> result = utilisateurController.getUsersByEquipeId(anyInt());

        assertTrue(((List) result).isEmpty());
        verify(utilisateurService, times(0)).getUtilisateursByEquipe(any());
    }

    @Test
    void shouldThrowExceptionWhenFileProcessingFails() {
        MultipartFile file = mock(MultipartFile.class);
        try {
            when(file.getInputStream()).thenThrow(new IOException());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThrows(IllegalArgumentException.class, () -> utilisateurController.processExcel(file));
    }
   @Test
    void shouldProcessExcel() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Test");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] bArray = bos.toByteArray();

        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(bArray));
        when(lectureExcelService.lireExcelUtilisateurs(any())).thenReturn(new ArrayList<>());
        when(lectureExcelService.lireExcelNotes(any())).thenReturn(new ArrayList<>());

        ResponseEntity<Map<List<Utilisateur>, List<NoteAnterieure>>> result = utilisateurController.processExcel(file);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void shouldProcessExcelUtilisateur() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Test");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] bArray = bos.toByteArray();

        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(bArray));
        when(lectureExcelService.lireExcelUtilisateurs(any())).thenReturn(new ArrayList<>());

        ResponseEntity<List<Utilisateur>> result = utilisateurController.processExcelUtilisateur(file);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void shouldExportIntoCSV() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        when(utilisateurService.getUtilisateurs()).thenReturn(new ArrayList<>());

        ResponseEntity<String> result = utilisateurController.exportIntoCSV(response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void shouldGetOLPL() {
        when(utilisateurService.getOLPL()).thenReturn(new ArrayList<>());

        List<Utilisateur> result = utilisateurController.getOLPL();

        assertTrue(result.isEmpty());
    }


}