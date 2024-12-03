package fr.eseo.backendalphaplan.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @file Role.java
 * @brief Classe permettant de définir un rôle
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Entity
@Data
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    private String description;

}
