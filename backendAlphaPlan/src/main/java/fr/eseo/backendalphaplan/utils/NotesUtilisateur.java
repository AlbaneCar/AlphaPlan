package fr.eseo.backendalphaplan.utils;

import lombok.Data;

/**
 * @file NotesUtilisateur.java
 * @brief Classe pour les notes des utilisateurs
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Data
public class NotesUtilisateur {

    // Attributs
    private String nom;
    private String prenom;

    private String prMa;
    private String teSo;
    private String spCo;
    private String suPr;
    private String teWo;
    private String teBm;
    private String ssBm;
    private String inSp;
    private String ssPr;
    private String otPr;
    private String inPr;
    private String igSp;


    // Constructeur
    public NotesUtilisateur(String nom, String prenom, String prMa, String teSo, String spCo, String suPr, String teWo, String teBm, String ssBm, String inSp, String ssPr, String otPr, String inPr, String igSp) {
        this.nom = nom;
        this.prenom = prenom;

        this.prMa = prMa;
        this.teSo = teSo;
        this.spCo = spCo;
        this.suPr = suPr;
        this.teWo = teWo;
        this.teBm = teBm;
        this.ssBm = ssBm;
        this.inSp = inSp;
        this.ssPr = ssPr;
        this.otPr = otPr;
        this.inPr = inPr;
        this.igSp = igSp;
    }
}
