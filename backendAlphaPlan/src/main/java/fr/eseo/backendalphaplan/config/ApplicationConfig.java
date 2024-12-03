package fr.eseo.backendalphaplan.config;

import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @file ApplicationConfig.java
 * @brief Classe de configuration de l'application
 * @version 1.0
 * @author Antoine MORIN
 * @date 30/05/2024
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    // Attributs
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private final UtilisateurRepository utilisateurRepository;

    /**
     * @brief Méthode permettant de définir un bean de type UserDetailsService
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> utilisateurRepository.findOptionalByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
    }

    /**
     * @brief Méthode permettant de définir un bean de type AuthenticationProvider
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * @brief Méthode permettant de définir un bean de type AuthenticationManager
     * @param config Configuration de l'authentification
     * @return AuthenticationManager
     * @throws Exception Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * @brief Classe interne NoOpPasswordEncoder
     * @details Classe permettant d'encoder le mot de passe et de le comparer
     */
    public static class NoOpPasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.toString().equals(encodedPassword);
        }
    }

    /**
     * @brief Méthode permettant de définir un bean de type PasswordEncoder
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
