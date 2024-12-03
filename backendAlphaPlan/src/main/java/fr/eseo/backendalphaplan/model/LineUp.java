package fr.eseo.backendalphaplan.model;

import fr.eseo.backendalphaplan.model.enums.StatusLineUp;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

/**
 * @file LineUp.java
 * @brief Définition de la classe LineUp.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 09/06/2024
 */
@Entity
@Data
public class LineUp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "auteurId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur auteur;

    @OneToOne
    @JoinColumn(name = "propositionId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur proposition;

    @JoinColumn(name = "status")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StatusLineUp status = StatusLineUp.ATTENTE;
}
