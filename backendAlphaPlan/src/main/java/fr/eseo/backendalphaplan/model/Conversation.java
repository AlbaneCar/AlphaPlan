package fr.eseo.backendalphaplan.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

/**
 * @file Conversation.java
 * @brief Définition de la classe Conversation.
 * @version 1.0
 * @author Yann LIDEC
 * @date 01/04/2024
 */
@Entity
@Data
public class Conversation {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "auteur_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur auteur;

    @Column(name = "titre", columnDefinition = "TEXT")
    private String titre;

    @ManyToMany
    @JoinTable(
        name = "conversation_utilisateur",
        joinColumns = @JoinColumn(name = "conversation_id"),
        inverseJoinColumns = @JoinColumn(name = "utilisateur_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Utilisateur> participants;

}
