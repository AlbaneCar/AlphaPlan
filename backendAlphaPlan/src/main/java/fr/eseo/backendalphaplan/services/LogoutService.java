package fr.eseo.backendalphaplan.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * @file LogoutService.java
 * @brief Service pour la déconnexion
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    /**
     * @brief Méthode pour déconnecter un utilisateur
     * @param request : requête HTTP
     * @param response : réponse HTTP
     * @param authentication : authentification
     */
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        response.setHeader("Authorization", "");
        SecurityContextHolder.clearContext();
    }
}
