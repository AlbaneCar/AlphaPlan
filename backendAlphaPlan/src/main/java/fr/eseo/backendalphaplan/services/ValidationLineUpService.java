package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.controller.LineUpController;
import fr.eseo.backendalphaplan.model.LineUp;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.ValidationLineUp;
import fr.eseo.backendalphaplan.model.enums.StatusLineUp;
import fr.eseo.backendalphaplan.repository.LineUpRepository;
import fr.eseo.backendalphaplan.repository.ValidationLineUpRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @file ValidationEchangeService.java
 * @brief Définition de la classe ValidationEchangeService.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 09/06/2024
 */
@Service
public class ValidationLineUpService {

    // Attributs
    private final UtilisateurRepository utilisateurRepository;
    private final ValidationLineUpRepository validationLineUpRepository;
    private final LineUpRepository lineUpRepository;

    // Constructeur
    @Autowired
    public ValidationLineUpService(
            UtilisateurRepository utilisateurRepository,
            ValidationLineUpRepository validationLineUpRepository,
            LineUpRepository lineUpRepository)
    {
        this.utilisateurRepository = utilisateurRepository;
        this.validationLineUpRepository = validationLineUpRepository;
        this.lineUpRepository = lineUpRepository;
    }

    /**
     * @brief Classe interne permettant de définir une requête de validation.
     * @param req : ValidationRequest
     * @throws IllegalArgumentException : Erreur si la requête est mal formulée
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    public void analyseRequest(LineUpController.ValidationRequest req) throws IllegalArgumentException {
        // On regarde si les attributs de la requête sont non nuls
        if (req.getAuteur() == null || req.getProposition() == null || req.getReponse() == null) {
            throw new IllegalArgumentException("Au moins un des arguments de la requête n'a pas été fournit !");
        }
        // On récupère les arguments de la requête
        Optional<Utilisateur> auteur = utilisateurRepository.findById(req.getAuteur());
        Optional<LineUp> lineUp = lineUpRepository.findById(req.getProposition());
        boolean reponse = req.getReponse();
        // On vérifie que les instances existent
        if (auteur.isEmpty() || lineUp.isEmpty()) {
            throw new IllegalArgumentException("Un des attributs spécifiés n'existe pas !");
        }
        // Sinon, on va mettre à jour le status de la requête de validation correspondante
        ValidationLineUp slot = validationLineUpRepository.findByLineUpAndUtilisateur(lineUp.get(), auteur.get());
        slot.setStatus((reponse) ? StatusLineUp.ACCEPTE : StatusLineUp.REFUSE);
        // On enregistre les modifications
        validationLineUpRepository.save(slot);
    }

    /**
     * @brief Classe interne permettant de définir une requête de validation.
     * @param lineUpId {@link Integer}
     * @return validations {@link List<ValidationLineUp>}
     * @throws IllegalArgumentException Erreur si la requête est mal formulée
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    public List<ValidationLineUp> getStatusOf(Integer lineUpId) throws IllegalArgumentException {
        // On regarde si l'argument n'est pas nul
        if (lineUpId == null) {
            throw new IllegalArgumentException("L'id passé en paramètre est null !");
        }
        // On récupère la line-up
        Optional<LineUp> lineUp = lineUpRepository.findById(lineUpId);
        // On regarde si elle n'est pas nulle
        if (lineUp.isEmpty()) {
            throw new IllegalArgumentException("Il n'existe pas de line-up avec cet id !");
        }
        // On retourne la liste des validations
        return validationLineUpRepository.findAllByLineUp(lineUp.get());
    }
}
