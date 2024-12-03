package fr.eseo.backendalphaplan;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @file ServletInitializer.java
 * @brief Initialisation du Servlet
 * @version 1.0
 * @date 01/04/2024
 */
public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * @brief Configure le Servlet
     * @param application : Application
     * @return Application
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BackendAlphaPlanApplication.class);
    }

}