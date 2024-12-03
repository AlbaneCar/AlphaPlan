package fr.eseo.backendalphaplan.model.enums;

/**
 * @file TypeNoteEquipe.java
 * @brief Définition de l'énumération TypeNoteEquipe.
 * @version 1.0
 * @author Hugo BOURDAIS & Antoine MORIN
 * @date 01/04/2024
 */
public enum TypeNoteEquipe {
    PR_MA, // "Project Management" : Note de gestion de projet
    TE_SO, // "Technical Solution" : Note de solution technique
    SP_CO, // "Sprint Conformity" : Note de conformité de sprint
    SU_PR, // "Support Presentation" : Note sur le support de la présentation

    TE_WO, // "Team Work" : Note de travail d'équipe, calculée par (PR_MA + TE_SO + SP_CO + SU_PR) / 4

    OT_PR // "Other Presentation" : Note de présentation de la part des autres membres du jury
}
