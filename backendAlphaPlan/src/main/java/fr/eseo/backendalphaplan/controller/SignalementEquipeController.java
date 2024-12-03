package fr.eseo.backendalphaplan.controller;

import fr.eseo.backendalphaplan.model.Notification;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeNotification;
import fr.eseo.backendalphaplan.services.SignalementEquipeService;
import fr.eseo.backendalphaplan.services.UtilisateurService;
import fr.eseo.backendalphaplan.utils.SignalementEquipeRequest;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @file SignalementEquipeController.java
 * @brief Classe controller de la gestion des signalements d'équipe
 * @version 1.0
 */
@RestController
@RequestMapping("/api/signalements")
public class SignalementEquipeController {

    // Attributs
    private final SignalementEquipeService signalementEquipeService;
    private final UtilisateurService utilisateurService;

    // Constructeur
    @Autowired
    public SignalementEquipeController(SignalementEquipeService signalementEquipeService, UtilisateurService utilisateurService) {
        this.signalementEquipeService = signalementEquipeService;
        this.utilisateurService = utilisateurService;
    }

    /**
     * @brief Enregistre un signalement d'équipe
     * @param response : réponse HTTP
     * @param signalementEquipeRequest : signalement d'équipe
     * @return ResponseEntity<Notification> : notification
     * @throws IOException : exception
     */
    @PostMapping
    public ResponseEntity<Notification> saveSignalementEquipe(
            HttpServletResponse response,
            @RequestBody SignalementEquipeRequest signalementEquipeRequest)
    throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(signalementEquipeRequest.getUtilisateurId());
        if(utilisateur != null && userDetails.getUsername().equals(utilisateur.getUsername())) {
            Notification signalementEquipe = new Notification();
            signalementEquipe.setMessage(signalementEquipeRequest.getDescription());
            signalementEquipe.setReceveur(utilisateurService.getUtilisateurByEmail("admin@mail.com"));
            signalementEquipe.setEnvoyeur(utilisateurService.getUtilisateurById(signalementEquipeRequest.getUtilisateurId()));
            signalementEquipe.setType(TypeNotification.RECLAMATION);
            return ResponseEntity.ok(signalementEquipeService.saveSignalementEquipe(signalementEquipe));
        } else {
            return new ResponseEntity<>(null, null, HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
