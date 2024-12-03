package fr.eseo.backendalphaplan.services;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.eseo.backendalphaplan.model.Role;
import fr.eseo.backendalphaplan.model.RoleUtilisateur;
import fr.eseo.backendalphaplan.model.enums.RoleType;
import fr.eseo.backendalphaplan.utils.UtilsGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.eseo.backendalphaplan.model.NoteAnterieure;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.Genre;
import fr.eseo.backendalphaplan.model.enums.Matiere;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;

/**
 * @file LectureExcelService.java
 * @brief Service pour la lecture des fichiers Excel
 * @version 1.0
 * @author Hugo R.
 * @date 01/04/2024
 */
@Service
public class LectureExcelService {

    // Attributs
    private final UtilisateurService utilisateurService;
    private final RoleUtilisateurService roleUtilisateurService;
    private final RoleService roleService;

    // Constructeur
    @Autowired
    public LectureExcelService(UtilisateurService utilisateurService, RoleUtilisateurService roleUtilisateurService, RoleService roleService) {
        this.utilisateurService = utilisateurService;
        this.roleUtilisateurService = roleUtilisateurService;
        this.roleService = roleService;
    }

    /**
     * @brief Fonction pour lire les utilisateurs du fichier Excel et les retourner
     * @param sheet : feuille Excel
     * @return Liste des utilisateurs
     */
    public List<Utilisateur> lireExcelUtilisateurs(Sheet sheet) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        int startRow = 2;
        for (int rowNum = startRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                Cell nomPrenomCell = row.getCell(1);
                if (nomPrenomCell == null || nomPrenomCell.getStringCellValue().isEmpty()) {
                    break;
                }
                Cell genreCell = row.getCell(2);
                Cell typeCell = createTypeCellIfAbsent(row);
                String contenuNom = getContenuNom(nomPrenomCell);
                String genre = getGenre(genreCell.getStringCellValue());
                TypeUtilisateur typeUtilisateur = getTypeUtilisateur(typeCell.getStringCellValue());
                String[] contenuSplit = contenuNom.split(" ");
                deleteExistingUser(contenuSplit);
                Utilisateur utilisateur = createUser(contenuSplit, genre, typeUtilisateur);
                utilisateurs.add(utilisateur);
            }
        }
        return utilisateurs;
    }

    /**
     * @brief Fonction pour créer une cellule de type si elle n'existe pas
     * @param row : ligne
     * @return Cellule de type
     */
    Cell createTypeCellIfAbsent(Row row) {
        Cell typeCell = row.getCell(3);
        if (typeCell == null) {
            typeCell = row.createCell(3);
            typeCell.setCellValue("E3e");
        }
        return typeCell;
    }

    /**
     * @brief Fonction pour récupérer le contenu du nom
     * @param nomPrenomCell : cellule du nom
     * @return Contenu du nom
     */
    String getContenuNom(Cell nomPrenomCell) {
        if (nomPrenomCell.getCellType() == CellType.STRING) {
            return nomPrenomCell.getStringCellValue();
        } else if (nomPrenomCell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) nomPrenomCell.getNumericCellValue());
        }
        return "";
    }

    /**
     * @brief Fonction pour récupérer le genre
     * @param contenuGenre : contenu du genre
     * @return Genre
     */
    String getGenre(String contenuGenre) {
        return contenuGenre.equals("F") ? String.valueOf(Genre.valueOf("FEMME")) : String.valueOf(Genre.valueOf("HOMME"));
    }

    /**
     * @brief Fonction pour récupérer le type de l'utilisateur
     * @param contenuType : contenu du type
     * @return Type de l'utilisateur
     */
    TypeUtilisateur getTypeUtilisateur(String contenuType) {
        return contenuType.equals("B") ? TypeUtilisateur.valueOf("BACHELOR") : TypeUtilisateur.valueOf("E3E");
    }

    /**
     * @brief Fonction pour supprimer un utilisateur existant
     * @param contenuSplit : contenu du nom et prénom
     */
    void deleteExistingUser(String[] contenuSplit) {
        Utilisateur utilisateurExistant = utilisateurService.findByNomAndPrenom(contenuSplit[0], contenuSplit[1]);

        // Si l'utilisateur n'existe pas, on ne fait rien
        if (utilisateurExistant == null) {
            return;
        }

        // On récupère le(s) rôle(s) de l'utilisateur
        List<RoleUtilisateur> roles = utilisateurService.getRoles(utilisateurExistant);

        // On supprime les rôles de l'utilisateur
        for (RoleUtilisateur role : roles) {
            roleUtilisateurService.deleteRoleUtilisateur(role);
        }
        // On supprime les notes antérieures de l'utilisateur
        utilisateurService.deleteNoteAnterieureByUtilisateur(utilisateurExistant);
        // On supprime l'utilisateur
        utilisateurService.deleteUtilisateur(utilisateurExistant);
    }

    /**
     * @brief Fonction pour créer un utilisateur
     * @param contenuSplit : contenu du nom et prénom
     * @param genre : genre
     * @param typeUtilisateur : type de l'utilisateur
     * @return Utilisateur
     */
    Utilisateur createUser(String[] contenuSplit, String genre, TypeUtilisateur typeUtilisateur) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(contenuSplit[0]);
        utilisateur.setPrenom(contenuSplit[1]);
        utilisateur.setGenre(Genre.valueOf(genre));
        utilisateur.setTypeUtilisateur(typeUtilisateur);
        utilisateur.setEmail(UtilsGenerator.generateMail(utilisateur.getNom() + " " + utilisateur.getPrenom()));
        utilisateur.setMotDePasse(UtilsGenerator.generatePassword(UtilsGenerator.generateUsername(utilisateur.getNom() + " " + utilisateur.getPrenom())));
        utilisateurService.saveUtilisateur(utilisateur);

        // On récupère le rôle "OPTION_STUDENT"
        Role role = roleService.getRoleByType(String.valueOf(RoleType.OPTION_STUDENT));
        RoleUtilisateur roleUtilisateur = new RoleUtilisateur();
        roleUtilisateur.setUtilisateur(utilisateur);
        roleUtilisateur.setRole(role);
        roleUtilisateurService.saveRoleUtilisateur(roleUtilisateur);

        return utilisateur;
    }

    // Attributs
    private Map<Integer, Matiere> colonneMatiereMapping = new HashMap<>();

    /**
     * @brief Fonction pour mapper les colonnes des matières
     */
    private void colonneMatiereMapping() {
        colonneMatiereMapping.put(4, Matiere.MOYENNE);
        colonneMatiereMapping.put(5, Matiere.PADL);
        colonneMatiereMapping.put(6, Matiere.PDLO);
        colonneMatiereMapping.put(7, Matiere.PWND);
        colonneMatiereMapping.put(8, Matiere.IRS);
        colonneMatiereMapping.put(9, Matiere.STAGE_S7);
        colonneMatiereMapping.put(10, Matiere.S5);
        colonneMatiereMapping.put(11, Matiere.S6);
    }

    /**
     * @brief Fonction pour récupérer un utilisateur à partir d'une ligne
     * @param row : ligne
     * @return Utilisateur
     */
    public Utilisateur getUtilisateurFromRow(Row row) {
        Cell nomPrenomCell = row.getCell(1);
        String nomPrenom;
        if (nomPrenomCell.getCellType() == CellType.NUMERIC) {
            nomPrenom = String.valueOf((int) nomPrenomCell.getNumericCellValue());
        } else {
            nomPrenom = nomPrenomCell.getStringCellValue();
        }
        String[] contenuSplit = nomPrenom.split(" ");
        String nom = contenuSplit[0];
        String prenom = contenuSplit.length > 1 ? contenuSplit[1] : "";
        return utilisateurService.findByNomAndPrenom(nom, prenom);
    }

    /**
     * @brief Fonction pour récupérer une note à partir d'une cellule
     * @param noteCell : cellule de la note
     * @param utilisateur : utilisateur
     * @param matiere : matière
     * @return Note antérieure
     */
    public NoteAnterieure getNoteFromCell(Cell noteCell, Utilisateur utilisateur, Matiere matiere) {
        if (noteCell != null && noteCell.getCellType() == CellType.NUMERIC) {
            double noteValue = noteCell.getNumericCellValue();
            NoteAnterieure noteAnterieure = new NoteAnterieure();
            noteAnterieure.setUtilisateur(utilisateur);
            noteAnterieure.setMatiere(matiere);
            noteAnterieure.setCoef((float) 1);
            noteAnterieure.setNote((float) noteValue);
            return noteAnterieure;
        }
        return null;
    }

    /**
     * @brief Fonction pour ajouter une note à une liste
     * @param notesAnterieures : liste des notes antérieures
     * @param noteAnterieure : note antérieure
     */
    public void addNoteToList(List<NoteAnterieure> notesAnterieures, NoteAnterieure noteAnterieure) {
        if (noteAnterieure != null) {
            notesAnterieures.add(noteAnterieure);
            utilisateurService.saveNoteAnterieure(noteAnterieure);
        }
    }

    /**
     * @brief Fonction pour lire les notes du fichier Excel et les retourner
     * @param sheet : feuille Excel
     * @return Liste des notes antérieures
     */
    public List<NoteAnterieure> lireExcelNotes(Sheet sheet) {
        List<NoteAnterieure> notesAnterieures = new ArrayList<>();
        colonneMatiereMapping();
        int startRow = 2;
        for (int rowNum = startRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                Utilisateur utilisateur = getUtilisateurFromRow(row);
                if (utilisateur != null) {
                    for (Map.Entry<Integer, Matiere> entry : colonneMatiereMapping.entrySet()) {
                        int columnIndex = entry.getKey();
                        Cell noteCell = row.getCell(columnIndex);
                        Matiere matiere = entry.getValue();
                        NoteAnterieure noteAnterieure = getNoteFromCell(noteCell, utilisateur, matiere);
                        addNoteToList(notesAnterieures, noteAnterieure);
                    }
                }
            }
        }
        return notesAnterieures;
    }

    /**
     * @brief Fonction pour initialiser le CSVPrinter
     * @param response : réponse HTTP
     * @return CSVPrinter
     * @throws IOException
     */
    public CSVPrinter initializeCsvPrinter(HttpServletResponse response) throws IOException {
        List<String> headers = Arrays.asList(" ", "Nom et Prénom", "Genre", "Type Utilisateur");
        CSVFormat csvFormat = CSVFormat.EXCEL.withDelimiter(';');
        Writer writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
        writer.write('\ufeff'); // Ajout d'un Byte Order Mark pour l'encodage UTF-8
        CSVPrinter printer = new CSVPrinter(writer, csvFormat);
        printer.printRecord("");
        printer.printRecord(headers);
        return printer;
    }

    /**
     * @brief Fonction pour générer un enregistrement utilisateur
     * @param utilisateur : utilisateur
     * @return Enregistrement utilisateur
     */
    public List<String> generateUserRecord(Utilisateur utilisateur) {
        List<String> enrigistrement = new ArrayList<>();
        enrigistrement.add("");
        enrigistrement.add(utilisateur.getNom() + " " + utilisateur.getPrenom());
        if (utilisateur.getGenre() == Genre.HOMME){
            enrigistrement.add("M");
        }
        else if (utilisateur.getGenre() == Genre.FEMME){
            enrigistrement.add("F");
        }else {
            enrigistrement.add(utilisateur.getGenre().name());
        }
        if (utilisateur.getTypeUtilisateur() == TypeUtilisateur.E3E) {
            enrigistrement.add(" ");
        } else if (utilisateur.getTypeUtilisateur() == TypeUtilisateur.BACHELOR) {
            enrigistrement.add("B");
        }
        return enrigistrement;
    }

    /**
     * @brief Fonction pour écrire les enregistrements dans le fichier CSV
     * @param utilisateurs : liste des utilisateurs
     * @param printer : CSVPrinter
     * @throw IOException
     */
    public void writeRecordsToCsv(List<Utilisateur> utilisateurs, CSVPrinter printer) throws IOException {
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getTypeUtilisateur() != TypeUtilisateur.E3E && utilisateur.getTypeUtilisateur() != TypeUtilisateur.BACHELOR) {
                continue;
            }
            List<String> enregistrement = generateUserRecord(utilisateur);
            printer.printRecord(enregistrement);
        }
        printer.flush();
    }

    /**
     * @brief Fonction pour exporter les utilisateurs au format CSV
     * @param utilisateurs : liste des utilisateurs
     * @param response : réponse HTTP
     * @throw IOException
     */
    public void exportUtilisateurCsv(List<Utilisateur> utilisateurs, HttpServletResponse response) throws IOException {
        CSVPrinter printer = initializeCsvPrinter(response);
        writeRecordsToCsv(utilisateurs, printer);
    }
}
