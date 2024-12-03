package fr.eseo.backendalphaplan.model.enums;

/**
 * @file TypeNote.java
 * @brief Enumération des types de notes possibles
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 29/04/2024
 */
public enum TypeNoteEleve {

    SS_PR, // "Supervising Staff Presentation" : Note de présentation de la part du staff de supervision
    IN_PR, // "Individual Presentation" : Note individuelle de présentation, calculée par (2 * SS_PR + OT_PR) / 3

    TE_BM, // "Team Bonus/Malus" : Bonus/Malus accordé par un membre de l'équipe
    SS_BM, // "Supervising Staff Bonus/Malus" : Bonus/Malus accordé par un membre du staff de supervision
    IN_SP, // "Individual Sprint" : Note individuelle de sprint, calculée par (TE_WO + Bonus/Malus)

    IG_SP // "Individual Grade Sprint" : Note individuelle de sprint, calculée par (0.7 * IN_SP + 0.3 * IN_PR)
}
