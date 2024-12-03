package fr.eseo.backendalphaplan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @file Message.java
 * @brief Définition de la classe Message.
 * @version 1.0
 * @author Hugo BOURDAIS & Antoine MORIN
 * @date 01/04/2024
 */
@Entity
@Data
public class Message {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Conversation conversation;
    
    @ManyToOne
    @JoinColumn(name = "auteur_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utilisateur auteur;
    
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

	public void setId(int i) {
		this.id = i;
	}

}
