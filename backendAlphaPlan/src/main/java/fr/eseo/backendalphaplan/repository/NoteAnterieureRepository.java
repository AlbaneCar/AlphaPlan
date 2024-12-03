package fr.eseo.backendalphaplan.repository;

import java.util.List;

import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.Matiere;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.eseo.backendalphaplan.model.NoteAnterieure;

/**
 * @file NoteAnterieureRepository.java
 * @brief Interface repository pour les NoteAnterieure
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface NoteAnterieureRepository extends JpaRepository<NoteAnterieure, Integer> {

    /**
     * @brief Requête pour récupérer la moyenne d'une équipe
     * @param idEquipe : id de l'équipe
     * @return Moyenne de l'équipe
     */
    @Query("SELECT (SUM(n.coef*n.note)/SUM(n.coef)) FROM NoteAnterieure n WHERE n.utilisateur.equipe.id = :idEquipe")
    double getMoyenneEquipe(Integer idEquipe);

    /**
     * @brief Requête pour récupérer la moyenne d'une matière
     * @param nomMatiere : nom de la matière
     * @return Moyenne de la matière
     */
    List<NoteAnterieure> findByMatiere(Matiere nomMatiere);

    /**
     * @brief Requête pour récupérer la moyenne d'un utilisateur
     * @param id : id de l'utilisateur
     * @return Moyenne de l'utilisateur
     */
    @Query("SELECT (SUM(n.coef*n.note)/SUM(n.coef)) FROM NoteAnterieure n WHERE n.utilisateur.id = :id")
    double getMoyenneUtilisateur(Integer id);

    /**
     * @brief Requête pour récupérer la liste des notes antérieures d'un utilisateur
     * @param id : id de l'utilisateur
     * @return Liste des notes antérieures
     */
    List<NoteAnterieure> findAllByUtilisateur_Id(int id);

    /**
     * @brief Requête pour récupérer la liste des notes antérieures d'un utilisateur
     * @param utilisateur : utilisateur
     * @return Liste des notes antérieures
     */
    List<NoteAnterieure> findByUtilisateur(Utilisateur utilisateur);

    /**
     * @brief Requête pour récupérer le coefficient d'une matière
     * @param matiere : matière
     * @return Coefficient de la matière
     */
    @Query("SELECT DISTINCT n.coef FROM NoteAnterieure n WHERE n.matiere = :matiere")
    int getCoef(Matiere matiere);
}