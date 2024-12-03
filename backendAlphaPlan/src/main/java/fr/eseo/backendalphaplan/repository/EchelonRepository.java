package fr.eseo.backendalphaplan.repository;

import fr.eseo.backendalphaplan.model.EchelleNote;
import fr.eseo.backendalphaplan.model.Echelon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface pour le repository des échelons.
 * Cette interface étend JpaRepository et définit des méthodes pour interagir avec la base de données.
 */
public interface EchelonRepository extends JpaRepository<Echelon, Integer> {

    /**
     * Méthode pour trouver les échelons par l'identifiant de l'échelle de notes.
     * @param echelleNoteId L'identifiant de l'échelle de notes.
     * @return Une liste des échelons correspondant à l'échelle de notes.
     */
    List<Echelon> findByEchelleNoteId(EchelleNote echelleNoteId);
}