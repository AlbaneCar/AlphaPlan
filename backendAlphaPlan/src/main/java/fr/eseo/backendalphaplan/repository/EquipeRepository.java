package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @file EquipeRepository.java
 * @brief Interface repository pour les Equipes
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface EquipeRepository extends JpaRepository<Equipe, Integer> {

    /**
     * @brief Requête pour récupérer les étudiants d'une équipe
     * @param id : id de l'équipe
     * @return Liste des étudiants
     */
    @Query("SELECT u FROM Utilisateur u WHERE u.equipe.id = :id")
    List<Utilisateur> getStudentsByEquipeId(Integer id);

    /**
     * @brief Requête pour récupérer une équipe via son nom
     * @param nom : nom de l'équipe
     * @return Equipe
     */
    @Query("SELECT e FROM Equipe e WHERE e.nom = :nom")
    Equipe findByNom(String nom);

    Equipe findByUtilisateur(Utilisateur user);
}
