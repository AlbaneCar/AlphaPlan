package fr.eseo.backendalphaplan.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @file ValidationEquipes.java
 * @brief Définition de la classe ValidationEquipes.
 * @version 1.0
 * @author Hugo BOURDAIS & Antoine MORIN
 * @date 01/04/2024
 */
@Entity
@Data
public class ValidationEquipes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "staff", referencedColumnName="id")
    private Utilisateur utilisateur;
}
