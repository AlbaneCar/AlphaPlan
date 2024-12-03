package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.controller.LoginController;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * @file AuthService.java
 * @brief Définition de la classe AuthService.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 01/04/2024
 */
@Service
@RequiredArgsConstructor
@Getter
@Setter
public class AuthService {

    // Attributs
    private final UtilisateurRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * @brief Méthode pour se connecter
     * @param email : email de l'utilisateur
     * @param password : mot de passe de l'utilisateur
     * @return LoginResponse
     */
    public LoginController.LoginResponse login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        Utilisateur utilisateur = userRepository.findOptionalByEmail(email)
                .orElseThrow();

        Equipe equipe = utilisateur.getEquipe();
        if (equipe == null) {
            return LoginController.LoginResponse.builder()
                    .token(jwtService.generateToken(utilisateur))
                    .id(utilisateur.getId())
                    .nom(utilisateur.getNom())
                    .prenom(utilisateur.getPrenom())
                    .roles(utilisateur.getRoles())
                    .build();
        } else {
            return LoginController.LoginResponse.builder()
                    .token(jwtService.generateToken(utilisateur))
                    .id(utilisateur.getId())
                    .nom(utilisateur.getNom())
                    .prenom(utilisateur.getPrenom())
                    .roles(utilisateur.getRoles())
                    .teamId(equipe.getId())
                    .build();
        }
    }
}
