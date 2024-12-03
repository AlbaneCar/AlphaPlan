package fr.eseo.backendalphaplan.model.enums;

/**
 * @file Genre.java
 * @brief Enumération des différents genres
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public enum Genre {
    HOMME,
    FEMME,
    AUTRE;

    /**
     * @brief Récupérer le genre à partir d'une chaîne de caractères
     * @param genre : Genre sous forme de chaîne de caractères
     * @return Genre
     */
    public static Genre getGenre(String genre) {
        return switch (genre) {
            case "HOMME" -> HOMME;
            case "FEMME" -> FEMME;
            case "AUTRE" -> AUTRE;
            default -> null;
        };
    }
}
