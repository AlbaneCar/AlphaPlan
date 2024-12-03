package fr.eseo.backendalphaplan.utils;

import lombok.Data;

/**
 * @file Note.java
 * @brief Classe Note
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 13/05/2024
 */
@Data
public class Note {

    // Attributes
    private String tag;
    private String nom;
    private String note;
    private String coeff;

    // Constructors
    public Note(String tag, String nom, String note, String coeff) {
        this.tag = tag;
        this.nom = nom;
        this.note = note;
        this.coeff = coeff;
    }
}
