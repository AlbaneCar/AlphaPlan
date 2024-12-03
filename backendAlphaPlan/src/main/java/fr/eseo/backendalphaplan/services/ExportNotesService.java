package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEquipe;
import fr.eseo.backendalphaplan.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @file ExportNotesService.java
 * @brief Classe de service pour l'export des notes des étudiants en format CSV
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 29/04/2024
 */
@Service
public class ExportNotesService {

    // Attributs
    private final EquipeRepository equipeRepository;
    private final NoteEleveRepository noteEleveRepository;
    private final NoteEquipeRepository noteEquipeRepository;
    private final SprintRepository sprintRepository;
    private final UtilisateurRepository utilisateurRepository;

    // Constructeur
     @Autowired
     public ExportNotesService(EquipeRepository equipeRepository, NoteEleveRepository noteEleveRepository, NoteEquipeRepository noteEquipeRepository, SprintRepository sprintRepository, UtilisateurRepository utilisateurRepository) {
         this.equipeRepository = equipeRepository;
         this.noteEleveRepository = noteEleveRepository;
         this.noteEquipeRepository = noteEquipeRepository;
         this.sprintRepository = sprintRepository;
         this.utilisateurRepository = utilisateurRepository;
     }

    /**
     * @brief Méthode permettant de récupérer les équipes.
     * @return List<Equipe> : Liste des équipes.
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private List<Equipe> getEquipes() {
        return equipeRepository.findAll();
    }

    /**
     * @brief Méthode permettant de récupérer les sprints.
     * @return List<Sprint> : Liste des sprints.
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private List<Sprint> getSprints() {
        return sprintRepository.findAll();
    }

    /**
     * @brief Méthode permettant de récupérer les étudiants d'une équipe.
     * @param equipe : Equipe
     * @return List<Utilisateur> : Liste des étudiants de l'équipe.
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private List<Utilisateur> getEtudiantsByEquipe(Equipe equipe) {
        return utilisateurRepository.findByEquipe(equipe);
    }

    /**
     * @brief Méthode permettant de générer un workbook Excel.
     * @param response : HttpServletResponse
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    public void getXSLX(HttpServletResponse response) throws IOException, NullPointerException {
        // Étape 1 : Récupération des équipes
        List<Equipe> equipes = getEquipes();
        if (equipes.isEmpty()) {
            throw new NullPointerException("Aucune équipe trouvée");
        }

        // Étape 2 :  Récupération des sprints
        List<Sprint> sprints = getSprints();
        if (sprints.isEmpty()) {
            throw new NullPointerException("Aucun sprint trouvé");
        }

        try(Workbook workbook = new XSSFWorkbook()) {

            // Étape 4 : Pour chaque Sprint on va créer une feuille Excel
            for (Sprint s : sprints) {
                this.generateSheet(s, workbook, equipes);
            }

            // Étape 5 : Écriture du classeur dans la réponse HTTP
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=notes_sprints.xlsx");
            workbook.write(response.getOutputStream());

        } catch (IOException e) {

            // Gestion des erreurs
            throw new IOException("Erreur lors de la génération du fichier Excel");

        }
    }

    /**
     * @brief Méthode permettant de générer une feuille Excel pour un sprint.
     * @param s : Sprint
     * @param workbook : Workbook
     * @param equipes : List<Equipe>
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private void generateSheet(Sprint s, Workbook workbook, List<Equipe> equipes) {
        // Création de la feuille Excel
        Sheet sheet = workbook.createSheet("Sprint " + s.getId());

        // On inialise un index pour les lignes
        int rowIndex = 0;

        // Pour chaque équipe on va ajouter les notes des étudiants
        for (Equipe e : equipes) {
            // On ajoute les notes des membres de l'équipe
            rowIndex = this.writeNotes(sheet, rowIndex, e, s);
        }
        sheet.autoSizeColumn(0, true);
    }

    /**
     * @brief Méthode permettant d'écrire les notes des étudiants dans une feuille Excel.
     * @param sheet : Sheet
     * @param rowIndex : int
     * @param equipe : Equipe
     * @return int : Index de la prochaine ligne
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private int writeNotes(Sheet sheet, int rowIndex, Equipe equipe, Sprint sprint) throws NullPointerException {
        // On ajoute l'entête de la feuille Excel
        rowIndex = this.setHeaders(sheet, rowIndex, equipe);

        // Récupération des étudiants de l'équipe
        List<Utilisateur> utilisateurs = getEtudiantsByEquipe(equipe);
        if (utilisateurs.isEmpty()) {
            throw new NullPointerException("Aucun étudiant trouvé pour l'équipe " + equipe.getNom());
        }

        // Pour chaque étudiant on va ajouter les notes dans la feuille Excel
        for (Utilisateur u : utilisateurs) {
            // On ajoute la ligne de l'étudiant
            this.setRow(sheet, rowIndex, u, sprint);
            rowIndex++;
        }

        // On saute une ligne pour séparer les équipes
        sheet.createRow(rowIndex);
        rowIndex++;

        // On retourne l'index de la prochaine ligne
        return rowIndex;
    }

    /**
     * @brief Méthode permettant de définir les entêtes de la feuille Excel.
     * @param sheet : Sheet
     * @param rowIndex : int
     * @param equipe : Equipe
     * @return int : Index de la prochaine ligne
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private int setHeaders(Sheet sheet, int rowIndex, Equipe equipe) {
        // Création de la ligne d'entête
        Row header = sheet.createRow(rowIndex);
        header.setHeight((short) 500);

        // On récupère les valeurs de l'énumération TypeNoteEquipe
        List<TypeNoteEquipe> typesNoteEquipe = List.of(TypeNoteEquipe.values());

        // On récupère les valeurs de l'énumération TypeNoteEleve
        List<TypeNoteEleve> typesNoteEleve = List.of(TypeNoteEleve.values());

        // Création d'un index pour les colonnes
        int colIndex = 0;

        // On initialise le style de l'entête
        CellStyle cellStyle = getHeaderStyle(sheet.getWorkbook());

        // On ajoute l'entête pour l'équipe
        Cell cellEquipe = header.createCell(colIndex);
        cellEquipe.setCellValue("Equipe " + equipe.getNom());
        cellEquipe.setCellStyle(cellStyle);
        colIndex++;

        // On ajoute les entêtes pour les notes d'équipe
        for (TypeNoteEquipe typeNoteEquipe : typesNoteEquipe) {
            Cell cell = header.createCell(colIndex);
            cell.setCellValue(typeNoteEquipe.toString());
            cell.setCellStyle(cellStyle);
            colIndex++;
        }
        for (TypeNoteEleve typeNoteEleve : typesNoteEleve) {
            Cell cell = header.createCell(colIndex);
            cell.setCellValue(typeNoteEleve.toString());
            cell.setCellStyle(cellStyle);
            colIndex++;
        }

        // On retourne l'index de la prochaine ligne
        return rowIndex + 1;
    }

    /**
     * @brief Méthode permettant de définir une ligne de la feuille Excel.
     * @param sheet : Sheet
     * @param index : int
     * @param utilisateur : Utilisateur
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private void setRow(Sheet sheet, int index, Utilisateur utilisateur, Sprint sprint) {
        Row row = sheet.createRow(index);

        // On récupère les valeurs de l'énumération TypeNoteEquipe
        List<TypeNoteEquipe> typesNoteEquipe = List.of(TypeNoteEquipe.values());

        // On récupère les valeurs de l'énumération TypeNoteEleve
        List<TypeNoteEleve> typesNoteEleve = List.of(TypeNoteEleve.values());

        // On initialise un index pour les colonnes
        int colIndex = 0;

        // On ajoute le nom et prénom de l'étudiant
        Cell cellEtudiant = row.createCell(colIndex);
        cellEtudiant.setCellValue(utilisateur.getPrenom() + " " + utilisateur.getNom());
        cellEtudiant.setCellStyle(this.getCellStyleFirst(sheet.getWorkbook()));
        colIndex++;

        // On récupère les notes de l'étudiant pour chaque type de note
        for (TypeNoteEquipe type : typesNoteEquipe) {
            Double note = noteEquipeRepository.findNoteByTypeAndEquipeAndSprint(type, utilisateur.getEquipe().getId(), sprint.getId());
            Cell cell = row.createCell(colIndex);
            cell.setCellStyle(this.getCellStyleClassic(sheet.getWorkbook()));
            if (note == null) {
                cell.setCellValue("ND");
            } else {
                cell.setCellValue(note);
            }
            colIndex++;
        }
        for (TypeNoteEleve type : typesNoteEleve) {
            Double note = noteEleveRepository.findNoteByTypeAndUtilisateurAndSprint(type, utilisateur.getId(), sprint.getId());
            Cell cell = row.createCell(colIndex);
            cell.setCellStyle((type != TypeNoteEleve.IG_SP) ? this.getCellStyleClassic(sheet.getWorkbook()) : this.getCellStyleFinal(sheet.getWorkbook()));
            if (note == null) {
                cell.setCellValue("ND");
            } else {
                cell.setCellValue(note);
            }
            colIndex++;
        }
    }

    /**
     * @brief Méthode permettant de définir le style de l'entête de la feuille Excel.
     * @param workbook : Workbook
     * @return CellStyle : Style de l'entête
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        // On initialise le style du texte
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        // On iniatialis le style de la cellule
        cellStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // On retourne le style
        return cellStyle;
    }

    /**
     * @brief Méthode permettant de définir le style de la cellule de la feuille Excel.
     * @param workbook : Workbook
     * @return CellStyle : Style de la cellule
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private CellStyle getCellStyleFirst(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        // On initialise le style du texte
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // On initialise le style de la cellule
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        // On retourne le style
        return cellStyle;
    }

    /**
     * @brief Méthode permettant de définir le style de la cellule de la feuille Excel.
     * @param workbook : Workbook
     * @return CellStyle : Style de la cellule
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private CellStyle getCellStyleClassic(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        // On initialise le style du texte
        Font font = workbook.createFont();
        font.setItalic(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // On initialise le style de la cellule
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        // On retourne le style
        return cellStyle;
    }

    /**
     * @brief Méthode permettant de définir le style de la cellule de la feuille Excel.
     * @param workbook : Workbook
     * @return CellStyle : Style de la cellule
     * @autor Hugo BOURDAIS
     * @date 29/04/2024
     */
    private CellStyle getCellStyleFinal(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        // On initialise le style du texte
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // On initialise le style de la cellule
        cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        // On retourne le style
        return cellStyle;
    }
}
