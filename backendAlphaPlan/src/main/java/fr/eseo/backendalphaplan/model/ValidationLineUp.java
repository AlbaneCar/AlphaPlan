package fr.eseo.backendalphaplan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eseo.backendalphaplan.model.enums.StatusLineUp;
import fr.eseo.backendalphaplan.model.id.UtilisateurLineUpId;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

/**
 * @file ValidationEchange.java
 * @brief Définition de la classe ValidationEchange.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 09/06/2024
 */
@Entity
@Data
@IdClass(UtilisateurLineUpId.class)
public class ValidationLineUp implements Serializable {

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "lineUpId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LineUp lineUp;

    @Id
    @ManyToOne
    @JoinColumn(name = "utilisateurId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur utilisateur;

    private StatusLineUp status;
}
