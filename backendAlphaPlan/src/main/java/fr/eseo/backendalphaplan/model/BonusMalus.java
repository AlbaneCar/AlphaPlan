package fr.eseo.backendalphaplan.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @file BonusMalus.java
 * @brief Classe permettant de définir un bonus ou un malus pour une note
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 * @see NoteEleve
 */
@Entity
@Data
public class BonusMalus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "valeur")
    private float valeur;

    @ManyToOne
    @JoinColumn(name = "note_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private NoteEleve noteEleve;
    
    @ManyToOne
    @JoinColumn(name = "evaluateur_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur evaluateur;

    @JoinColumn(name = "commentaire")
    private String commentaire;
    
    @JoinColumn(name = "nbValidations")
    private int nbValidations;
    
    @JoinColumn(name = "isValide")
    private boolean isValide;

	public void setIsValide(Boolean iv) {
		this.isValide = iv;
	}
}
