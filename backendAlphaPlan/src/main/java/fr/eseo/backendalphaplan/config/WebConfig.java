package fr.eseo.backendalphaplan.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @file WebConfig.java
 * @brief Définition de la classe WebConfig.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 01/04/2024
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // Attributs
    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

    /**
     * @brief Méthode permettant de configurer les CORS.
     * @param registry : CorsRegistry : Registre des CORS.
     * @autor Hugo BOURDAIS
     * @date 01/04/2024
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configuration des CORS
        registry
            .addMapping("/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true);
    }

    /**
     * @brief Méthode permettant de configurer les vues.
     * @param registry : ViewControllerRegistry : Registre des vues.
     * @autor Antoine MORIN
     * @date 30/05/2024
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Map "/"
        String forwardIndexString = "forward:/index.html";
        registry.addViewController("/")
                .setViewName(forwardIndexString);

        // Single directory level - no need to exclude "api"
        registry.addViewController("/{x:[\\w\\-]+}")
                .setViewName(forwardIndexString);
        // Multi-level directory path, need to exclude "api" on the first part of the path
        registry.addViewController("/{x:^(?!api$).*$}/**/{y:[\\w\\-]+}")
                .setViewName(forwardIndexString);
    }
}