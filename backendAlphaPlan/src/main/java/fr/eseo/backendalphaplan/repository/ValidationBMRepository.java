package fr.eseo.backendalphaplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.eseo.backendalphaplan.model.BonusMalus;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.ValidationBM;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;

/**
 * @file ValidationBMRepository.java
 * @brief Interface repository pour les ValidationBM
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface ValidationBMRepository extends JpaRepository<ValidationBM, Integer> {

    /**
     * @brief Requête pour récupérer le nombre de validations d'un utilisateur pour un type de note, un sprint et une équipe donnée
     * @param id : id de l'utilisateur
     * @param type : type de note
     * @param sprint : numéro du sprint
     * @param equipe : id de l'équipe
     * @return Nombre de validations
     */
    @Query("SELECT COUNT(v.id) FROM ValidationBM v WHERE v.bonusMalus.noteEleve.eleve.equipe.id = :equipe AND v.bonusMalus.noteEleve.sprint.id = :sprint AND v.bonusMalus.noteEleve.typeNoteEleve = :type AND v.utilisateur.id = :id")
    int getNbValidationsBM(Integer id, TypeNoteEleve type, int sprint, int equipe);

    /**
     * @brief Requête pour vérifier si une validation existe pour un bonus/malus et un utilisateur donné
     * @param bonusMalus : bonus/malus
     * @param utilisateur : utilisateur
     * @return boolean
     */
    boolean existsByBonusMalusAndUtilisateur(BonusMalus bonusMalus, Utilisateur utilisateur);
}
