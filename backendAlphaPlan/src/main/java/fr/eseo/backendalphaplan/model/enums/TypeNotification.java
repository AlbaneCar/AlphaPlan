package fr.eseo.backendalphaplan.model.enums;

/**
 * @file TypeNotification.java
 * @brief Définition de l'énumération TypeNotification
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public enum TypeNotification {
    AIDE,
    RECLAMATION,
    PASSAGE,
    NOUVEAU_BM,
    BM_SUPPRIME,
    BM_ACCEPTE,
    NOUVELLE_CONVERSATION,
    NOUVEAU_MESSAGE,
    CONVERSATION_SUPPRIMEE,
    EQUIPES,
    CHANGEMENT_LINEUP,
    DEFAULT;


    /**
     * @brief Méthode permettant de récupérer le type de notification
     * @param type : type de la notification
     * @return TypeNotification
     */
    public static TypeNotification getType(String type) {
        for (TypeNotification typeNotification : TypeNotification.values()) {
            if (typeNotification.name().equals(type)) {
                return typeNotification;
            }
        }
        return DEFAULT;
    }
}
