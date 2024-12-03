package fr.eseo.backendalphaplan.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

/**
 * @file JwtService.java
 * @brief Service pour la gestion des tokens JWT
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class JwtService {

    // Constantes
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expirationInMs}")
    private Long jwtExpiration;

    /**
     * @brief Extrait le nom d'utilisateur du token
     * @param token : token
     * @return Nom d'utilisateur
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * @brief Extrait une information du token
     * @param token : token
     * @param claimsResolver : fonction de récupération de l'information
     * @return Information
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * @brief Extrait toutes les informations du token
     * @param token : token
     * @return Informations
     */
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * @brief Récupère la clé de signature
     * @return Clé de signature
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * @brief Vérifie si le token est valide
     * @param token : token
     * @param utilisateur : utilisateur
     * @return Vrai si le token est valide, faux sinon
     */
    public boolean validateToken(String token, UserDetails utilisateur) {
        final String username = extractUsername(token);
        return (username.equals(utilisateur.getUsername()) && !isTokenExpired(token));
    }

    /**
     * @brief Vérifie si le token est expiré
     * @param token : token
     * @return Vrai si le token est expiré, faux sinon
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * @brief Extrait la date d'expiration du token
     * @param token : token
     * @return Date d'expiration
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * @brief Génère un token
     * @param utilisateur : utilisateur
     * @return Token
     */
    public String generateToken(UserDetails utilisateur) {
        return buildToken(new HashMap<>(), utilisateur, jwtExpiration);
    }

    /**
     * @brief Génère un token avec des informations supplémentaires
     * @param extraClaims : informations supplémentaires
     * @param utilisateur : utilisateur
     * @return Token
     */
    private String buildToken(
            HashMap<String, Object> extraClaims,
            UserDetails utilisateur,
            Long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(utilisateur.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * @brief Rafraîchit un token
     * @param token : token
     * @return Token rafraîchi
     */
    public String setExpiredToken(String token) {
        final Claims claims = extractAllClaims(token);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
