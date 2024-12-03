package fr.eseo.backendalphaplan.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * @file UtilsGenerator.java
 * @brief Classe utilitaire pour générer des données
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public class UtilsGenerator {

    // Attributs
    private static final String MAIL = "@mail.com";

    /**
     * @brief Supprimer les accents
     * @param text : Texte
     * @return Texte sans accents
     */
    public static String removeAccents(String text) {
        String nfdNormalizedString = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    /**
     * @brief Générer un mail
     * @param name : Nom
     * @return Mail
     */
    public static String generateMail(String name) {
        // On remplace les caractères spéciaux par des caractères normaux
        String nameWithoutAccents = removeAccents(name);
        // On supprime les "'" et les "-"
        nameWithoutAccents = nameWithoutAccents.replaceAll("['-]", "");
        // On sépare le nom et le prénom via " "
        String[] nameParts = nameWithoutAccents.split(" ");
        // On récupère le prénom
        String firstName = nameParts[1].toLowerCase();
        // On récupère le nom
        String lastName = nameParts[0].toLowerCase();
        // On concatène les 2 parties pour obtenir le mail
        return firstName + "." + lastName + UtilsGenerator.MAIL;

    }

    /**
     * @brief Générer un nom d'utilisateur
     * @param name : Nom
     * @return Nom d'utilisateur
     */
    public static String generateUsername(String name) {
        // On remplace les caractères spéciaux par des caractères normaux
        String nameWithoutAccents = removeAccents(name);
        // On supprime les "'" et les "-"
        nameWithoutAccents = nameWithoutAccents.replaceAll("['-]", "");
        // On sépare le nom et le prénom via " "
        String[] nameParts = nameWithoutAccents.split(" ");
        // On récupère le prénom et on garde les 3 premières lettres
        String firstName = nameParts[1].substring(0, 3);
        // On met le prénom en minuscule sauf la première lettre
        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        // On récupère le nom et on garde les 3 premières lettres
        String lastName = nameParts[0].substring(0, 3);
        // On met le nom en minuscule sauf la première lettre
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
        // On concatène les 2 parties pour obtenir le nom d'utilisateur
        return lastName + firstName;

    }

    /**
     * @brief Générer un mot de passe
     * @param password : Mot de passe
     * @return Mot de passe crypté
     */
    public static String generatePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    /**
     * @brief Générer une clé secrète
     */
    public static void generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom(); //creates a secure random number generator
        byte[] key = new byte[64]; //creates a byte array with the specified number of bytes
        secureRandom.nextBytes(key); //generates random bytes and places them into the byte array
        String secretKey = Base64.getEncoder().encodeToString(key); //returns the Base64 encoded string
        System.out.println("Secret Key: " + secretKey); //NOSONAR
    }

    /**
     * @brief Décoder un mot de passe
     * @param password : Mot de passe
     * @param encodedPassword : Mot de passe crypté
     * @return Message
     */
    public static String decodePassword(String password, String encodedPassword) {
        return BCrypt.checkpw(password, encodedPassword) ? "Le mot de passe est correct" : "Le mot de passe est incorrect";
    }

    /**
     * @brief Main
     * @param args : Arguments
     */
    public static void main(String[] args) {
        System.out.println(decodePassword("AbaCyr", "$2a$10$jCowPNEoLJQJjrS7L7wKnOTgOxxutcf0c4NMRMjjFwCZmRVNoIaju")); //NOSONAR
    }
}