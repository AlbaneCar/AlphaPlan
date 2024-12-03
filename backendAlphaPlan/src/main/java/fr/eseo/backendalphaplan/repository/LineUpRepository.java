package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.LineUp;
import fr.eseo.backendalphaplan.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @file LineUpRepository.java
 * @brief Définition de la classe LineUpRepository.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 09/06/2024
 */
public interface LineUpRepository extends JpaRepository<LineUp, Integer> {

    List<LineUp> findByAuteur(Utilisateur utilisateur);
}
