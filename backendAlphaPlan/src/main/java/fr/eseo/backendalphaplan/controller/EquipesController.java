package fr.eseo.backendalphaplan.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.EtatEquipes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.services.EquipeService;
import fr.eseo.backendalphaplan.dto.UtilisateurDTO;
import fr.eseo.backendalphaplan.services.NoteAnterieureService;

/**
 * @file EquipesController.java
 * @brief La classe EquipesController est le controller qui permet de gérer les requêtes HTTP concernant les équipes
 * @version 1.0
 */
@RestController
@RequestMapping("/api/equipes")
public class EquipesController {

    // Attributs
    private final EquipeService equipeService;
    private final NoteAnterieureService noteAnterieureService;

    // Constructeur
    @Autowired
    public EquipesController(EquipeService equipeService, NoteAnterieureService noteAnterieureService) {
        this.equipeService = equipeService;
        this.noteAnterieureService = noteAnterieureService;

    }

    /**
     * @brief Méthode permettant de récupérer toutes les équipes
     * @return Liste des équipes
     */
    @GetMapping
    public List<Equipe> getEquipes() {
    	return equipeService.getEquipes();
    }

    /**
     * @brief Méthode permettant de récupérer une équipe par son id
     * @param id : id de l'équipe
     * @return Equipe
     */
    @GetMapping("/{id}")
    public Equipe getEquipeById(@PathVariable Integer id) {
        return equipeService.getEquipeById(id);
    }

    @GetMapping("/{id}/referent")
    public Equipe getEquipeReferent(@PathVariable Integer id) {
        return equipeService.getEquipeReferentById(id);
    }

    /**
     * @brief Méthode permettant de creer des équipes
     * @param nbEquipes : nombre d'équipes à créer
     * @return Liste des équipes
     */
    @GetMapping("/creerEquipes/{nbEquipes}")
    public ResponseEntity<List<List<UtilisateurDTO>>> creerEquipes(@PathVariable Integer nbEquipes) {
        List<List<Utilisateur>> equipes = equipeService.creerEquipes(nbEquipes);

        // Convertir les utilisateurs en DTO
        List<List<UtilisateurDTO>> equipesDTO = equipes.stream()
                .map(equipe -> equipe.stream()
                        .map(this::mapUtilisateurToDTO)
                        .toList())
                .toList();

        return ResponseEntity.ok(equipesDTO);
    }

    /**
     * @brief Méthode permettant de mapper un utilisateur en DTO
     * @param utilisateur : utilisateur à mapper
     * @return UtilisateurDTO
     */
    private UtilisateurDTO mapUtilisateurToDTO(Utilisateur utilisateur) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(utilisateur.getId());
        dto.setNom(utilisateur.getNom());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setMoyenne(noteAnterieureService.getMoyenneUtilisateur(utilisateur.getId()));
        dto.setGenre(utilisateur.getGenre());
        dto.setTypeUtilisateur(utilisateur.getTypeUtilisateur());
        return dto;
    }

    /**
     * @brief Méthode d'envoi des équipes
     * @param requestBody : requête HTTP
     * @param etatEquipes : état des équipes
     * @return Validation de l'envoi des équipes
     */
    @PostMapping("/envoyer/{etatEquipes}")
    public ResponseEntity<Map<String, String>> sendTeams(@RequestBody String requestBody, @PathVariable EtatEquipes etatEquipes) {
        equipeService.processJsonAndAssignUsers(requestBody, etatEquipes);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Données reçues avec succès !");
        return ResponseEntity.ok(response);
    }

    /**
     * @brief Méthode permettant de changer l'état des équipes
     * @param etatEquipes : état des équipes
     * @return Validation du changement de l'état des équipes
     */
    @PutMapping("/etatEquipes/{etatEquipes}")
    public ResponseEntity<Map<String, String>> changeTeamsState(@PathVariable EtatEquipes etatEquipes) {
        equipeService.changeTeamsState(etatEquipes);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Données reçues avec succès !");
        return ResponseEntity.ok(response);
    }
}
