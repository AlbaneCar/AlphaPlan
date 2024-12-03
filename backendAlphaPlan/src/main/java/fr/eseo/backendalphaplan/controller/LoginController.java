package fr.eseo.backendalphaplan.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.eseo.backendalphaplan.model.RoleUtilisateur;
import fr.eseo.backendalphaplan.services.AuthService;

import jakarta.annotation.security.PermitAll;

import lombok.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @file LoginController.java
 * @brief Définition de la classe LoginController.
 * @version 1.0
 * @author Hugo BOURDAIS & Antoine MORIN
 * @date 01/04/2024
 */
@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@PermitAll
public class LoginController {
    private final AuthService authService;

    /**
     * @brief Méthode permettant de se connecter à l'application.
     * @param loginRequest : LoginRequest : Requête contenant l'email et le mot de passe de l'utilisateur.
     * @return ResponseEntity<LoginResponse> : Réponse HTTP contenant le jwt de l'utilisateur.
     * @autor Hugo BOURDAIS & Antoine MORIN
     * @date 01/04/2024
     */
    @PostMapping("/login") // Définition de la route
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    /**
     * @brief Classe interne permettant de définir une requête de connexion.
     * @autor Hugo BOURDAIS & Antoine MORIN
     * @date 01/04/2024
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static final class LoginRequest {
        private String email;
        private String password;
    }

    /**
     * @brief Classe interne permettant de définir une réponse de connexion.
     * @autor Hugo BOURDAIS & Antoine MORIN
     * @date 01/04/2024
     */
    @Data
    @AllArgsConstructor
    @Builder
    @RequiredArgsConstructor
    public static final class LoginResponse {
        @JsonProperty("access_token")
        private String token;
        private Integer id;
        private String prenom;
        private String nom;
        private List<RoleUtilisateur> roles;
        private Integer teamId;
    }
}
