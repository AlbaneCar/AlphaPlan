package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.ValidationEquipes;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import fr.eseo.backendalphaplan.repository.ValidationEquipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @file ValidationEquipesService.java
 * @brief Service pour les ValidationEquipes
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
public class ValidationEquipesService {

    // Attributs
    private final ValidationEquipesRepository validationEquipesRepository;
    private final UtilisateurRepository utilisateurRepository;

    // Constructeur
    @Autowired
    public ValidationEquipesService(ValidationEquipesRepository validationEquipesRepository, UtilisateurRepository utilisateurRepository) {
        this.validationEquipesRepository = validationEquipesRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * @brief Créer une ValidationEquipe
     * @param utilisateurId : Id de l'utilisateur
     * @return ValidationEquipes
     */
    public ValidationEquipes createValidationEquipe(Integer utilisateurId) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateurId);
        if (utilisateurOptional.isPresent()) {
            ValidationEquipes validationEquipes = new ValidationEquipes();
            validationEquipes.setUtilisateur(utilisateurOptional.get());
            return validationEquipesRepository.save(validationEquipes);
        } else {
            throw new IllegalArgumentException("Utilisateur not found with id: " + utilisateurId);
        }
    }

    /**
     * @brief Récupérer les ValidationEquipes
     * @return Liste des ValidationEquipes
     */
    public List<ValidationEquipes> getAllValidationEquipes() {
        return validationEquipesRepository.findAll();
    }

    /**
     * @brief Supprimer les ValidationEquipes
     */
    public void deleteAllValidationEquipes() {
        validationEquipesRepository.deleteAll();
    }
}
