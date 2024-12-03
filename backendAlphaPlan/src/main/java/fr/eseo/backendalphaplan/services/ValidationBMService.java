package fr.eseo.backendalphaplan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eseo.backendalphaplan.model.ValidationBM;
import fr.eseo.backendalphaplan.model.enums.TypeNoteEleve;
import fr.eseo.backendalphaplan.repository.ValidationBMRepository;

/**
 * @file ValidationBMService.java
 * @brief Classe service pour les ValidationBM
 * @version 1.0
 * @date 01/04/2024
 */
@Service
public class ValidationBMService {
	
    // Attributs
    private final ValidationBMRepository validationBMRepository;

    // Constructeur
    @Autowired
    public ValidationBMService(ValidationBMRepository validationBMRepository) {
        this.validationBMRepository = validationBMRepository;
    }

    /**
     * @brief Sauvegarde une validationBM
     * @param v : ValidationBM
     * @return ValidationBM
     */
    public ValidationBM saveValidationBM(ValidationBM v) {
        if (validationBMRepository.existsByBonusMalusAndUtilisateur(v.getBonusMalus(), v.getUtilisateur())) {
            return null;
        }
        return validationBMRepository.save(v);
    }  

    /**
     * @brief Récupère le nombre de validations d'un bonus/malus
     * @param id : id du bonus/malus
     * @param type : type de la note
     * @param sprint : numéro du sprint
     * @param equipe : id de l'équipe
     * @return int : nombre de validations
     */
    public int getNbValidationsBM(Integer id, TypeNoteEleve type, int sprint, int equipe) {
    	return validationBMRepository.getNbValidationsBM(id, type, sprint, equipe);
    }
}
