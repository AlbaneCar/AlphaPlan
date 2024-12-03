package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.EchelleNote;
import fr.eseo.backendalphaplan.model.enums.TypeEchelle;
import fr.eseo.backendalphaplan.repository.EchelleNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @file EchelleNoteService.java
 * @brief Service pour les EchelleNote
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
public class EchelleNoteService {

    // Attributs
    private final EchelleNoteRepository echelleNoteRepository;

    // Constructeur
    @Autowired
    public EchelleNoteService(EchelleNoteRepository echelleNoteRepository) {
        this.echelleNoteRepository = echelleNoteRepository;
    }
    public EchelleNote getEchelleNoteById(Integer id) {
        return echelleNoteRepository.findEchelleById(id);
    }

    /**
     * @brief Récupérer une échelle de note via son type
     * @param typeEchelle : type de l'échelle
     * @return EchelleNote
     */
    public EchelleNote getEchelleNoteByTyep(TypeEchelle typeEchelle) {
        return echelleNoteRepository.findByTypeEchelle(typeEchelle);
    }

    /**
     * @brief Récupérer une échelle de note via son id
     * @param typeEchelle : type de l'échelle
     * @return EchelleNote
     */
    public EchelleNote getEchelleNoteById(TypeEchelle typeEchelle) {
        return echelleNoteRepository.findByTypeEchelle(typeEchelle);
    }

    /**
     * @brief Ajouter une échelle de note
     * @param typeEchelle : type de l'échelle
     * @param description : description de l'échelle
     * @return EchelleNote
     */
    public EchelleNote ajouterEchelle(TypeEchelle typeEchelle, String description){
        EchelleNote echelleNote = new EchelleNote();
        echelleNote.setTypeEchelle(typeEchelle);
        echelleNote.setDescription(description);
        return echelleNoteRepository.save(echelleNote);
    }

    /**
     * @brief Modifier une échelle de note
     * @param id : id de l'échelle
     * @param typeEchelle : type de l'échelle
     * @param description : description de l'échelle
     * @return EchelleNote
     */
    public EchelleNote modifierEchelle(Integer id, TypeEchelle typeEchelle, String description){
        EchelleNote echelleNote = echelleNoteRepository.findEchelleById(id);
        echelleNote.setTypeEchelle(typeEchelle);
        echelleNote.setDescription(description);
        return echelleNoteRepository.save(echelleNote);
    }

    /**
     * @brief Supprimer une échelle de note
     * @param id : id de l'échelle
     */
    public void supprimerEchelle(Integer id){
        echelleNoteRepository.deleteById(id);
    }

    /**
     * @brief Récupérer toutes les échelles de note
     * @return Liste des EchelleNote
     */
    public List<EchelleNote> getAllEchelleNotes() {
        return echelleNoteRepository.findAll();
    }
}

