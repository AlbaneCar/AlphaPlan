package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.EchelleNote;
import fr.eseo.backendalphaplan.model.Echelon;
import fr.eseo.backendalphaplan.repository.EchelonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service pour gérer les échelons.
 * Ce service utilise le repository des échelons pour interagir avec la base de données.
 */
@Service
public class EchelonService {
    private EchelonRepository echelonRepository;

    /**
     * Constructeur pour injecter le repository des échelons.
     * @param echelonRepository Le repository des échelons.
     */
    @Autowired
    public EchelonService(EchelonRepository echelonRepository) {
        this.echelonRepository = echelonRepository;
    }

    /**
     * Méthode pour obtenir les échelons par l'identifiant de l'échelle de notes.
     * @param echelleNote L'échelle de notes.
     * @return Une liste des échelons correspondant à l'échelle de notes.
     */
    public List<Echelon> getEchelonsByEchelleNoteId(EchelleNote echelleNote) {
        return echelonRepository.findByEchelleNoteId(echelleNote);
    }

    /**
     * Méthode pour sauvegarder un échelon.
     * @param echelon L'échelon à sauvegarder.
     */
    public void save(Echelon echelon) {
        echelonRepository.save(echelon);
    }

    /**
     * Méthode pour supprimer un échelon.
     * @param echelon L'échelon à supprimer.
     */
    public void delete(Echelon echelon) {
        echelonRepository.delete(echelon);
    }
}