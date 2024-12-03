package fr.eseo.backendalphaplan.model;

import fr.eseo.backendalphaplan.model.enums.EtatEquipes;
import jakarta.persistence.*;

import lombok.Data;

import java.io.Serializable;

/**
 * @file Equipe.java
 * @brief Classe permettant de définir une équipe
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Entity
@Data
public class Equipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "nom")
    private String nom;

    @OneToOne
    @JoinColumn(name = "referent", referencedColumnName="id")
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    private EtatEquipes etatEquipes;
}
