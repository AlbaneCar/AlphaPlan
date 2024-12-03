package fr.eseo.backendalphaplan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eseo.backendalphaplan.model.id.UtilisateurRoleId;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

/**
 * @file RoleUtilisateur.java
 * @brief Classe permettant de définir un rôle d'un utilisateur
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 * @see Utilisateur
 * @see Role
 */
@Entity
@Table(name = "role_utilisateur")
@IdClass(UtilisateurRoleId.class)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RoleUtilisateur implements Serializable {

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "utilisateur_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur utilisateur;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Role role;
}
