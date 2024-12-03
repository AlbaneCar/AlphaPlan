package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.*;
import fr.eseo.backendalphaplan.model.enums.Genre;
import fr.eseo.backendalphaplan.services.EquipeService;
import fr.eseo.backendalphaplan.services.LectureExcelService;
import fr.eseo.backendalphaplan.services.NoteAnterieureService;
import fr.eseo.backendalphaplan.services.UtilisateurService;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @file UserController.java
 * @brief Définition de la classe UserController.
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@RestController // Définition de la classe comme contrôleur
@RequestMapping("/api/users") // Définition de la route
public class UtilisateurController {

    // Attributs
    private final UtilisateurService utilisateurService;
    private final EquipeService equipeService;
    private final NoteAnterieureService noteAnterieureService;
    private final LectureExcelService lectureExcelService;

    // Constructeur
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, EquipeService equipeService, NoteAnterieureService noteAnterieureService, LectureExcelService lectureExcelService) {
        this.utilisateurService = utilisateurService;
        this.equipeService = equipeService;
        this.noteAnterieureService = noteAnterieureService;
        this.lectureExcelService = lectureExcelService;
    }

    /**
     * @brief Méthode permettant de récupérer tous les utilisateurs.
     * @return Iterable<Utilisateur> : Liste des utilisateurs récupérés.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    @GetMapping()
    public Iterable<Utilisateur> getAllUsers() {
        return utilisateurService.getUtilisateurs();
    }

    /**
     * @brief Méthode permettant de récupérer tous les étudiants.
     * @return Page<Utilisateur> : Page des étudiants récupérés.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    @GetMapping("/students")
    public Page<Utilisateur> getAllStudents(Pageable pageable) {
        return utilisateurService.getStudents(pageable);
    }

    /**
     * @brief Méthode permettant de récupérer un utilisateur par son identifiant.
     * @param id : Integer : Identifiant de l'utilisateur.
     * @return Utilisateur : Utilisateur récupéré.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    @GetMapping("/{id}") // Définition de la route
    public Utilisateur getUserById(@PathVariable Integer id) {
        return utilisateurService.getUtilisateurById(id);
    }

    /**
     * @brief Méthode permettant de récupérer les notes antérieures d'un utilisateur.
     * @param id : Integer : Identifiant de l'utilisateur.
     * @return List<NoteAnterieure> : Liste des notes antérieures récupérées.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    @GetMapping("/{id}/notes")
    public List<NoteAnterieure> getNotesAnterieures(@PathVariable int id) {
        return noteAnterieureService.getAllNotesAnterieures(id);
    }

    /**
     * @brief Méthode permettant d'ajouter un utilisateur.
     * @param requestBody : Map<String, String> : Informations de l'utilisateur à ajouter.
     * @return Utilisateur : Utilisateur ajouté.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    @PostMapping()
    public Utilisateur addPerson(@RequestBody Map<String, String> requestBody) {

        // Étape 1 : Création de l'utilisateur avec les informations de la requête
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(requestBody.get("nom"));
        utilisateur.setPrenom(requestBody.get("prenom"));
        utilisateur.setEmail(requestBody.get("email"));
        utilisateur.setMotDePasse(requestBody.get("password"));
        utilisateur.setGenre(Genre.valueOf(requestBody.get("genre")));

        // Étape 2 : Récupération de l'équipe
        Equipe equipe = equipeService.getEquipeById(Integer.parseInt(requestBody.get("equipe_id")));

        // Étape 3 : Ajout de l'équipe à l'utilisateur
        utilisateur.setEquipe(equipe);

        // Étape 4 : Sauvegarde de l'utilisateur
        return utilisateurService.saveUtilisateur(utilisateur);
    }

    /**
     * @brief Méthode permettant d'enregistrer les données d'utilisateurs à partir d'un fichier Excel pour le PL
     * @param file : MultipartFile : Fichier Excel contenant les données des utilisateurs.
     * @return ResponseEntity<Map<List<Utilisateur>,List<NoteAnterieure>>> : Liste des utilisateurs et des notes antérieures.
     * @autor Hugo ROULLIER
     * @date 01/04/2024
     */
    @PostMapping("/processExcel")
    public ResponseEntity<Map<List<Utilisateur>,List<NoteAnterieure>>> processExcel(@RequestParam("file") MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<Utilisateur> utilisateurs = lectureExcelService.lireExcelUtilisateurs(sheet);
            List<NoteAnterieure> notesAnterieures = lectureExcelService.lireExcelNotes(sheet);
            return ResponseEntity.ok(Map.of(utilisateurs, notesAnterieures));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @brief Méthode permettant d'enregistrer les données d'utilisateurs à partir d'un fichier Excel pour l'OL
     * @param file : MultipartFile : Fichier Excel contenant les données des utilisateurs.
     * @return ResponseEntity<List<Utilisateur>> : Liste des utilisateurs.
     * @autor Hugo ROULLIER
     * @date 01/04/2024
     */
    @PostMapping("/processExcelUtilisateur")
    public ResponseEntity<List<Utilisateur>> processExcelUtilisateur(@RequestParam("file") MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<Utilisateur> utilisateurs = lectureExcelService.lireExcelUtilisateurs(sheet);
            return ResponseEntity.ok(utilisateurs);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @brief Méthode permettant d'exporter les données d'utilisateurs dans un fichier CSV
     * @param response : HttpServletResponse : Réponse HTTP.
     * @return ResponseEntity<String> : Message de succès ou d'erreur.
     * @autor Hugo ROULLIER
     * @date 01/04/2024
     */
    @GetMapping("/exportCSV")
    public ResponseEntity<String> exportIntoCSV(HttpServletResponse response) {
        try {
            response.setContentType("text/csv");
            response.addHeader("Content-Disposition", "attachment; filename=\"Etudiant.csv\"");
            lectureExcelService.exportUtilisateurCsv(utilisateurService.getUtilisateurs(), response);
            return ResponseEntity.ok("Exported successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * @brief Méthode permettant de récupérer les utilisateurs d'une équipe.
     * @param equipeId : Integer : Identifiant de l'équipe.
     * @return Iterable<Utilisateur> : Liste des utilisateurs récupérés.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    @GetMapping("/equipes/{equipeId}")
    public Iterable<Utilisateur> getUsersByEquipeId(@PathVariable Integer equipeId) {
        Equipe equipe = equipeService.getEquipeById(equipeId);
        if (equipe != null) {
            return utilisateurService.getUtilisateursByEquipe(equipe);
        } else {
            // handle the case where the equipe with the given id doesn't exist
            return new ArrayList<>();
        }
    }

    /**
     * @brief Méthode permettant de récupérer les encadrants.
     * @return List<Utilisateur> : Liste des encadrants récupérés.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    @GetMapping("/encadrants")
    public List<Utilisateur> getEncadrants() {
        return utilisateurService.getEncadrants();
    }

    /**
     * @brief Méthode permettant de récupérer les étudiants.
     * @return List<Utilisateur> : Liste des étudiants récupérés.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    @GetMapping("/admins")
    public List<Utilisateur> getOLPL() {
        return utilisateurService.getOLPL();
    }


    /**
     * @brief Méthode permettant de récupérer les rôles de l'utilisateur connecté.
     * @return List<String> : Liste des rôles de l'utilisateur connecté.
     * @autor MORIN Antoine
     * @date 01/04/2024
     */
    @GetMapping("/roles")
    public List<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(userDetails.getUsername());
        return utilisateur.getRoles().stream()
                .map(RoleUtilisateur::getRole)
                .map(Role::getNom)
                .collect(Collectors.toList());
    }
}