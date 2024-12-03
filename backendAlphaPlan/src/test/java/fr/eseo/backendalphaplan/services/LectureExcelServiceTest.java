package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.NoteAnterieure;
import fr.eseo.backendalphaplan.model.RoleUtilisateur;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.Genre;
import fr.eseo.backendalphaplan.model.enums.Matiere;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;
import fr.eseo.backendalphaplan.repository.NoteAnterieureRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LectureExcelServiceTest {

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private RoleUtilisateurService roleUtilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private NoteAnterieureRepository noteAnterieureRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private Sheet sheet;

    @Mock
    private Row row;

    @Mock
    private Cell cell;


    @InjectMocks
    private LectureExcelService lectureExcelService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Ne devrait pas importer les notes d'Excel si la cellule de note est vide")
    @Test
    void impossibleImportNotesExcelSiCelluleNoteVide() {
        Sheet sheet = mock(Sheet.class);
        Row row = mock(Row.class);
        when(sheet.getRow(anyInt())).thenReturn(row);

        Cell cellNomPrenom = mock(Cell.class);
        Cell cellType = mock(Cell.class);
        Cell cellNote = mock(Cell.class);

        when(row.getCell(1)).thenReturn(cellNomPrenom);
        when(row.getCell(3)).thenReturn(cellType);
        when(row.getCell(anyInt())).thenReturn(cellNote);

        when(cellNomPrenom.getStringCellValue()).thenReturn("John Doe");
        when(cellType.getStringCellValue()).thenReturn("E3e");
        doReturn(Double.NaN).when(cellNote).getNumericCellValue();

        lectureExcelService.lireExcelNotes(sheet);

        verify(utilisateurService, never()).saveNoteAnterieure(any(NoteAnterieure.class));
    }

    @DisplayName("Ne devrait pas importer les notes d'Excel si l'utilisateur n'existe pas")
    @Test
    void impossibleImporterNotesSiUtilisateurExistePasTest() {

        Sheet sheet = mock(Sheet.class);
        Row row = mock(Row.class);
        when(sheet.getRow(anyInt())).thenReturn(row);

        Cell cellNomPrenom = mock(Cell.class);
        Cell cellType = mock(Cell.class);
        Cell cellNote = mock(Cell.class);

        when(row.getCell(1)).thenReturn(cellNomPrenom);
        when(row.getCell(3)).thenReturn(cellType);
        when(row.getCell(anyInt())).thenReturn(cellNote);

        when(cellNomPrenom.getStringCellValue()).thenReturn("Non Existent User");
        when(cellType.getStringCellValue()).thenReturn("E3e");
        when(cellNote.getNumericCellValue()).thenReturn(15.0);

        when(utilisateurService.findByNomAndPrenom(anyString(), anyString())).thenReturn(null);

        lectureExcelService.lireExcelNotes(sheet);

        verify(utilisateurService, never()).saveNoteAnterieure(any(NoteAnterieure.class));
    }

    @DisplayName("Peut exporter les utilisateurs au format CSV")
    @Test
    void exporterUtilisateursSansExceptions() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        List<Utilisateur> utilisateurs = new ArrayList<>();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("John");
        utilisateur.setPrenom("Doe");
        utilisateur.setGenre(Genre.HOMME);
        utilisateur.setTypeUtilisateur(TypeUtilisateur.E3E);
        utilisateurs.add(utilisateur);

        assertDoesNotThrow(() -> lectureExcelService.exportUtilisateurCsv(utilisateurs, response));
    }

    @DisplayName("Envoie une exception quand l'export échoue")
    @Test
    void exceptionsExporterFails() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Utilisateur> utilisateurs = new ArrayList<>();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("John");
        utilisateur.setPrenom("Doe");
        utilisateur.setGenre(Genre.HOMME);
        utilisateur.setTypeUtilisateur(TypeUtilisateur.E3E);
        utilisateurs.add(utilisateur);

        doThrow(IOException.class).when(response).getOutputStream();

        assertThrows(IOException.class, () -> lectureExcelService.exportUtilisateurCsv(utilisateurs, response));
    }

    @DisplayName("Devrait récupérer un utilisateur depuis une ligne")
    @Test
    void testGetUtilisateurOnExcel() {
        Row row = mock(Row.class);
        Cell cellNomPrenom = mock(Cell.class);
        when(row.getCell(1)).thenReturn(cellNomPrenom);
        when(cellNomPrenom.getStringCellValue()).thenReturn("John Doe");

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("John");
        utilisateur.setPrenom("Doe");

        when(utilisateurService.findByNomAndPrenom("John", "Doe")).thenReturn(utilisateur);

        Utilisateur result = lectureExcelService.getUtilisateurFromRow(row);

        assertNotNull(result);
        assertEquals("John", result.getNom());
        assertEquals("Doe", result.getPrenom());
    }

    @DisplayName("Devrait récupérer une note depuis une cellule")
    @Test
    void testGetNoteExcel() {
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.NUMERIC);
        when(cell.getNumericCellValue()).thenReturn(15.0);

        Utilisateur utilisateur = new Utilisateur();
        Matiere matiere = Matiere.MOYENNE;

        NoteAnterieure note = lectureExcelService.getNoteFromCell(cell, utilisateur, matiere);

        assertNotNull(note);
        assertEquals(15.0, note.getNote());
        assertEquals(utilisateur, note.getUtilisateur());
        assertEquals(matiere, note.getMatiere());
    }

    @DisplayName("Devrait ajouter une note à la liste")
    @Test
    void testAddNote() {
        List<NoteAnterieure> notesAnterieures = new ArrayList<>();
        NoteAnterieure noteAnterieure = new NoteAnterieure();

        lectureExcelService.addNoteToList(notesAnterieures, noteAnterieure);

        assertEquals(1, notesAnterieures.size());
        assertEquals(noteAnterieure, notesAnterieures.get(0));
        verify(utilisateurService, times(1)).saveNoteAnterieure(noteAnterieure);
    }

    @DisplayName("Devrait supprimer un utilisateur déjà existant")
    @Test
    void deleteExistingUserTest() {
        String[] contenuSplit = {"John", "Doe"};
        Utilisateur existingUser = new Utilisateur();
        existingUser.setNom("John");
        existingUser.setPrenom("Doe");

        when(utilisateurService.findByNomAndPrenom("John", "Doe")).thenReturn(existingUser);

        lectureExcelService.deleteExistingUser(contenuSplit);

        verify(utilisateurService, times(1)).deleteUtilisateur(existingUser);
    }

    @DisplayName("devrait créer un utilisateur")
    @Test
    void createUserTest() {
        String[] contenuSplit = {"John", "Doe"};
        String genre = "HOMME";
        TypeUtilisateur typeUtilisateur = TypeUtilisateur.E3E;

        Utilisateur result = lectureExcelService.createUser(contenuSplit, genre, typeUtilisateur);

        assertNotNull(result);
        assertEquals("John", result.getNom());
        assertEquals("Doe", result.getPrenom());
        assertEquals(Genre.HOMME, result.getGenre());
        assertEquals(TypeUtilisateur.E3E, result.getTypeUtilisateur());
        verify(utilisateurService, times(1)).saveUtilisateur(any(Utilisateur.class));
    }

    @DisplayName("Lit les utilisateurs depuis une feuille Excel")
    @Test
    void lireExcelUtilisateursTest() {
        Sheet sheet = mock(Sheet.class);
        Row row = mock(Row.class);
        when(sheet.getRow(anyInt())).thenReturn(row);

        Cell cellNomPrenom = mock(Cell.class);
        Cell cellGenre = mock(Cell.class);
        Cell cellType = mock(Cell.class);

        when(row.getCell(1)).thenReturn(cellNomPrenom);
        when(row.getCell(2)).thenReturn(cellGenre);
        when(row.getCell(3)).thenReturn(cellType);

        when(cellNomPrenom.getStringCellValue()).thenReturn("John Doe");
        when(cellGenre.getStringCellValue()).thenReturn("M");
        when(cellType.getStringCellValue()).thenReturn("E3e");

        when(utilisateurService.findByNomAndPrenom("John", "Doe")).thenReturn(null);

        List<Utilisateur> result = lectureExcelService.lireExcelUtilisateurs(sheet);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetTypeUtilisateur() {
        TypeUtilisateur result = lectureExcelService.getTypeUtilisateur("B");
        assertEquals(TypeUtilisateur.BACHELOR, result);
    }

    @Test
    void testGetContenuNom() {
        when(cell.getCellType()).thenReturn(CellType.STRING);
        when(cell.getStringCellValue()).thenReturn("John Doe");
        String result = lectureExcelService.getContenuNom(cell);
        assertEquals("John Doe", result);
    }




    @Test
    //antoine
    void testCreateTypeCellIfAbsent_CellExists() {
        Cell existingCell = mock(Cell.class);
        when(row.getCell(3)).thenReturn(existingCell);

        Cell result = lectureExcelService.createTypeCellIfAbsent(row);

        assertSame(existingCell, result);
        verify(row).getCell(3);
        verify(row, never()).createCell(3);
        verify(existingCell, never()).setCellValue(anyString());
    }

    @Test
    void testCreateTypeCellIfAbsent_CellDoesNotExist() {
        when(row.getCell(3)).thenReturn(null);
        Cell newCell = mock(Cell.class);
        when(row.createCell(3)).thenReturn(newCell);

        Cell result = lectureExcelService.createTypeCellIfAbsent(row);

        assertSame(newCell, result);
        verify(row).getCell(3);
        verify(row).createCell(3);
        verify(newCell).setCellValue("E3e");
    }

    @Test
    void testGetContenuNom_CellTypeString() {
        when(cell.getCellType()).thenReturn(CellType.STRING);
        when(cell.getStringCellValue()).thenReturn("John Doe");

        String result = lectureExcelService.getContenuNom(cell);

        assertEquals("John Doe", result);
        verify(cell).getCellType();
        verify(cell).getStringCellValue();
    }

    @Test
    void testGetContenuNom_CellTypeNumeric() {
        when(cell.getCellType()).thenReturn(CellType.NUMERIC);
        when(cell.getNumericCellValue()).thenReturn(123.0);

        String result = lectureExcelService.getContenuNom(cell);

        assertEquals("123", result);
        verify(cell, times(2)).getCellType();
        verify(cell).getNumericCellValue();
    }

    @Test
    void testGetContenuNom_CellTypeOther() {
        when(cell.getCellType()).thenReturn(CellType.BOOLEAN);

        String result = lectureExcelService.getContenuNom(cell);

        assertEquals("", result);
        verify(cell, times(2)).getCellType();
    }

    @Test
    void testGetGenre_Female() {
        String result = lectureExcelService.getGenre("F");
        assertEquals("FEMME", result);
    }

    @Test
    void testGetGenre_Male() {
        String result = lectureExcelService.getGenre("M");
        assertEquals("HOMME", result);
    }

    @Test
    void testTypUtilisateur_E3e() {
        TypeUtilisateur result = lectureExcelService.getTypeUtilisateur("E3e");
        assertEquals(TypeUtilisateur.E3E, result);
    }

    @Test
    void testTypUtilisateur_Bachelor() {
        TypeUtilisateur result = lectureExcelService.getTypeUtilisateur("B");
        assertEquals(TypeUtilisateur.BACHELOR, result);
    }


    @Test
    void testDeleteExistingUser_UserNull() {
        String[] contenuSplit = {"John", "Doe"};
        when(utilisateurService.findByNomAndPrenom("John", "Doe")).thenReturn(null);

        lectureExcelService.deleteExistingUser(new String[]{contenuSplit[0], contenuSplit[1]});

        verify(utilisateurService).findByNomAndPrenom(contenuSplit[0], contenuSplit[1]);
        verifyNoMoreInteractions(utilisateurService);
    }

    @Test
    void testDeleteExistingUser_UserNotNull() {
        String[] contenuSplit = {"John", "Doe"};
        List<RoleUtilisateur> roles = Arrays.asList(
                new RoleUtilisateur(),
                new RoleUtilisateur(),
                new RoleUtilisateur()
        );
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurService.findByNomAndPrenom("John", "Doe")).thenReturn(utilisateur);
        when(utilisateurService.getRoles(utilisateur)).thenReturn(roles);

        lectureExcelService.deleteExistingUser(new String[]{contenuSplit[0], contenuSplit[1]});

        for(RoleUtilisateur role : roles) {
            verify(roleUtilisateurService, times(3)).deleteRoleUtilisateur(role);
        }

        verify(utilisateurService).findByNomAndPrenom(contenuSplit[0], contenuSplit[1]);
        verify(utilisateurService).deleteUtilisateur(utilisateur);
    }


    @Test
    public void testLireExcelUtilisateurs_NomPrenomCellNull() {
        when(sheet.getLastRowNum()).thenReturn(3);
        Row rowMock = mock(Row.class);
        when(sheet.getRow(2)).thenReturn(rowMock);
        when(rowMock.getCell(1)).thenReturn(null);

        List<Utilisateur> result = lectureExcelService.lireExcelUtilisateurs(sheet);

        assertTrue(result.isEmpty(), "The list should be empty when nomPrenomCell is null");
    }

    @Test
    public void testLireExcelUtilisateurs_NomPrenomCellEmpty() {
        when(sheet.getLastRowNum()).thenReturn(3);
        Row rowMock = mock(Row.class);
        Cell cellMock = mock(Cell.class);
        when(sheet.getRow(2)).thenReturn(rowMock);
        when(rowMock.getCell(1)).thenReturn(cellMock);
        when(cellMock.getStringCellValue()).thenReturn("");



        List<Utilisateur> result = lectureExcelService.lireExcelUtilisateurs(sheet);

        assertTrue(result.isEmpty(), "The list should be empty when nomPrenomCell is empty");
    }



    @Test
    public void testLireExcelUtilisateurs_ValidData() throws NoSuchMethodException {

        Sheet sheetMock = mock(Sheet.class);
        Row rowMock = mock(Row.class);
        Cell cellMock = mock(Cell.class);


        when(sheetMock.getLastRowNum()).thenReturn(2);
        when(sheetMock.getRow(anyInt())).thenReturn(rowMock);
        when(rowMock.getCell(anyInt())).thenReturn(cellMock);
        when(cellMock.getStringCellValue()).thenReturn("Doe John", "Homme", "Encadrant");
        when(cellMock.getStringCellValue()).thenReturn("Doe John");

        when(cellMock.getCellType()).thenReturn(CellType.STRING);


        List<Utilisateur> result = lectureExcelService.lireExcelUtilisateurs(sheetMock);

        // Vérification des résultats
        assertFalse(result.isEmpty(), "The list should not be empty when valid data is provided");
        assertEquals(1, result.size(), "The list should contain one user when one valid row is processed");

        // Vérifier les valeurs de l'utilisateur créé
        Utilisateur utilisateur = result.get(0);
        assertEquals("John", utilisateur.getPrenom(), "The first name should be 'John'");
        assertEquals("Doe", utilisateur.getNom(), "The last name should be 'Doe'");
        assertEquals("HOMME", utilisateur.getGenre().toString(), "The genre should be 'Homme'");
        assertEquals("E3E", utilisateur.getTypeUtilisateur().toString(), "The user type should be 'Encadrant'");
    }
}
