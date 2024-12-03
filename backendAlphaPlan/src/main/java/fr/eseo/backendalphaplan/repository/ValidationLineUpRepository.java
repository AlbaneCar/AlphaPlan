package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.LineUp;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.ValidationLineUp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @file ValidationLineUpRepository.java
 * @brief Définition de la classe ValidationLineUpRepository.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 09/06/2024
 */
public interface ValidationLineUpRepository extends JpaRepository<ValidationLineUp, Integer> {

    ValidationLineUp findByLineUpAndUtilisateur(LineUp lineUp, Utilisateur auteur);

    List<ValidationLineUp> findAllByLineUp(LineUp lineUp);
}
