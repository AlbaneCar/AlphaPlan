package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.ValidationEquipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @file ValidationEquipesRepository.java
 * @brief Interface repository pour les ValidationEquipes
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Repository
public interface ValidationEquipesRepository extends JpaRepository<ValidationEquipes, Integer> {
}
