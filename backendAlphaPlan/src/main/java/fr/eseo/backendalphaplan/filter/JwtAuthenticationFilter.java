package fr.eseo.backendalphaplan.filter;

import fr.eseo.backendalphaplan.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @file JwtAuthenticationFilter.java
 * @brief Définition de la classe JwtAuthenticationFilter.
 * @version 1.0
 * Antoine MORIN
 * @date 01/04/2024
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    // Attributs
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    /**
     * @brief Méthode permettant de filtrer les requêtes HTTP.
     * @param request : HttpServletRequest : Requête HTTP.
     * @param response : HttpServletResponse : Réponse HTTP.
     * @param filterChain : FilterChain : Chaîne de filtres.
     * @throws ServletException : Exception levée lorsqu'une erreur se produit lors du traitement de la requête.
     * @throws IOException : Exception levée lorsqu'une erreur se produit lors de la lecture ou de l'écriture d'un fichier.
     * @autor Antoine MORIN
     * @date 01/04/2024
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if(request.getServletPath().contains("assets")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getServletPath().contains("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String header = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = header.substring(7);
        try {
            userEmail = jwtService.extractUsername(jwt);
        }catch (Exception e) {
            setResponseInvalidError(response);
            return;
        }

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            boolean isTokenValid = jwtService.validateToken(jwt, userDetails);
            if(isTokenValid) {
                logger.info("Request to : " + request.getServletPath());
                logger.info("Token : " + jwt);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                setResponseInvalidError(response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


    private void setResponseInvalidError(HttpServletResponse response) throws IOException {
        response.setStatus(403); // Utilisation d'un code d'état valide
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = "{\"message\": \"Token invalid\"}";
        response.getOutputStream().print(jsonResponse);
    }
}
