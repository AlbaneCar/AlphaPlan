package fr.eseo.backendalphaplan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

/**
 * @file ValidationBM.java
 * @brief Définition de la classe ValidationBM.
 * @version 1.0
 * @author Hugo BOURDAIS & Antoine MORIN
 * @date 01/04/2024
 */
@Entity
@Data
@Table(name = "validationbm", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_bm", "id_utilisateur"})
})
public class ValidationBM {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_bm", nullable = false)
    private BonusMalus bonusMalus;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;
}
