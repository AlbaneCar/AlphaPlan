package fr.eseo.backendalphaplan.utils;


import lombok.*;

/**
 * @file SignalementEquipeRequest.java
 * @brief Classe pour les signalements des équipes
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SignalementEquipeRequest {
    private String description;
    private int equipeId;
    private int utilisateurId;
}
