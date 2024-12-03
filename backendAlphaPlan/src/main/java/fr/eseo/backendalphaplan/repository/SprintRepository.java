package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.Sprint;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

/**
 * @file SprintRepository.java
 * @brief Interface repository pour les Sprint
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface SprintRepository extends JpaRepository<Sprint, Integer> {

    /**
     * @brief Requête pour récupérer un sprint via son nom
     * @param nom : nom du sprint
     * @return Sprint
     */
    @Query("SELECT s FROM Sprint s WHERE s.name = :nom")
    Sprint findByName(String nom);

    /**
     * @brief Requête pour récupérer le sprint en cours
     * @param date : date actuelle
     * @return Sprint
     */
    @Query("SELECT s FROM Sprint s WHERE :date BETWEEN s.startDate AND s.endDate")
    Optional<Sprint> findCurrentSprint(@Param("date") LocalDate date);
}
