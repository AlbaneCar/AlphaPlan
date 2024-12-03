package fr.eseo.backendalphaplan.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eseo.backendalphaplan.model.NoteAnterieure;
import fr.eseo.backendalphaplan.model.enums.Matiere;
import fr.eseo.backendalphaplan.repository.NoteAnterieureRepository;

/**
 * @file NoteAnterieureService.java
 * @brief Classe service pour les NoteAnterieure
 * @version 1.0
 * @date 01/04/2024
 * @author Enzo HERBRETEAU
 */
@Service
public class NoteAnterieureService {

    // Attributs
    private final NoteAnterieureRepository noteAnterieureRepository;

    // Constructeur
    @Autowired
    public NoteAnterieureService(NoteAnterieureRepository noteAnterieureRepository) {
        this.noteAnterieureRepository = noteAnterieureRepository;
    }

    /**
     * @brief Méthode récupérant la moyenne d'une équipe via son id
     * @param id : id de l'équipe
     * @return Moyenne de l'équipe
     */
    public double getMoyenneEquipe(Integer id) {
        return noteAnterieureRepository.getMoyenneEquipe(id);
    }

    /**
     * @brief Méthode récupérant la moyenne d'un utilisateur via son id
     * @param id : id de l'utilisateur
     * @return Moyenne de l'utilisateur
     */
    public double getMoyenneUtilisateur(Integer id) {
        if(this.getAllNotesAnterieures(id).isEmpty()){
            return 0.0;
        }else{
            return noteAnterieureRepository.getMoyenneUtilisateur(id);
        }
    }

    /**
     * @brief Méthode modifiant le coefficient d'une matière via son nom et le nouveau coefficient
     * @param nomMatiere : nom de la matière
     * @param nouveauCoef : nouveau coefficient
     */
    public void modifierCoefMatiere(Matiere nomMatiere, Float nouveauCoef) {
        List<NoteAnterieure> notes = noteAnterieureRepository.findByMatiere(nomMatiere);
        for (NoteAnterieure note : notes) {
            note.setCoef(nouveauCoef);
        }
        noteAnterieureRepository.saveAll(notes);
    }

    /**
     * @brief Méthode récupérant toutes les notes antérieures d'un utilisateur via son id
     * @param id : id de l'utilisateur
     * @return Liste des notes antérieures
     */
    public List<NoteAnterieure> getAllNotesAnterieures(Integer id) {
        return noteAnterieureRepository.findAllByUtilisateur_Id(id);
    }

    /**
     * @brief Méthode récupérant le coefficient d'une matière via son nom
     * @param matiere : nom de la matière
     * @return Coefficient de la matière
     */
    public int getCoefMatiere(Matiere matiere) {
        return noteAnterieureRepository.getCoef(matiere);
    }
}
