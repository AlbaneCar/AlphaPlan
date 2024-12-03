package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.EchelleNote;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @file EchelleNoteRepository.java
 * @brief Interface repository pour les EchelleNote
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
public interface EchelleNoteRepository extends JpaRepository<EchelleNote, Integer> {

    /**
     * @brief Requête pour récupérer une échelle de note via son type
     * @param typeEchelle : type de l'échelle
     * @return EchelleNote
     */
    @Query("SELECT e FROM EchelleNote e WHERE e.typeEchelle = :typeEchelle")
    EchelleNote findByTypeEchelle(TypeEchelle typeEchelle);

    /**
     * @brief Requête pour supprimer une échelle de note via son type
     * @param typeEchelle : type de l'échelle
     */
    @Modifying
    @Query("DELETE FROM EchelleNote e WHERE e.typeEchelle = :typeEchelle")
    void deleteByTypeEchelle(TypeEchelle typeEchelle);

    @Query("SELECT e FROM EchelleNote e WHERE e.id = :id")
    EchelleNote findEchelleById(Integer id);
}