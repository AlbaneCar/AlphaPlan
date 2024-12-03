package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.controller.LineUpController;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.LineUp;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.ValidationLineUp;
import fr.eseo.backendalphaplan.model.enums.StatusLineUp;
import fr.eseo.backendalphaplan.repository.LineUpRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import fr.eseo.backendalphaplan.repository.ValidationLineUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @file LineUpService.java
 * @brief Définition de la classe LineUpService.
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 09/06/2024
 */
@Service
public class LineUpService {

    // Attributs
    private final LineUpRepository lineUpRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ValidationLineUpRepository validationLineUpRepository;

    // Constructeur
    @Autowired
    public LineUpService(
            LineUpRepository lineUpRepository,
            UtilisateurRepository utilisateurRepository,
            ValidationLineUpRepository validationLineUpRepository)
    {
        this.lineUpRepository = lineUpRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.validationLineUpRepository = validationLineUpRepository;
    }

    /**
     * @brief Méthode permettant de créer une propostion de line-up.
     * @param req {@link LineUpController.LineUpRequest}
     * @return {@link LineUp}
     * @throws IllegalArgumentException Exception si la requête est mal formulée
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    public LineUp createLineUp(LineUpController.LineUpRequest req) throws IllegalArgumentException {
        // On regarde d'abord si les arguments de la requête existent
        if (req.getAuteur() == null || req.getProposition() == null) {
            throw new IllegalArgumentException("Au moins un des arguments est absent !");
        }
        // On récupère les attributs
        Optional<Utilisateur> auteur = utilisateurRepository.findById(req.getAuteur());
        Optional<Utilisateur> propostion = utilisateurRepository.findById(req.getProposition());
        // On regarde s'ils existent
        if (auteur.isEmpty() || propostion.isEmpty()) {
            throw new IllegalArgumentException("Au moins une des personnes rensignées n'existe pas !");
        }
        // On regarde si l'auteur à déjà fait une propostion
        if (!lineUpRepository.findByAuteur(auteur.get()).isEmpty()) {
            throw new IllegalArgumentException("Une même personne ne peut pas soumettre plusieurs propositions !");
        }
        // On va créer la propostion de line-up
        LineUp lineUp = new LineUp();
        lineUp.setAuteur(auteur.get());
        lineUp.setProposition(propostion.get());
        // On enregistre dans la BDD
        lineUpRepository.save(lineUp);
        // Pour chacun des membres des équipes, on va créer une demande de validation
        try {
            this.createValidationLineUp(auteur.get(), lineUp);
            this.createValidationLineUp(propostion.get(), lineUp);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        // On renvoie la line-up créée
        return lineUp;
    }

    /**
     * @brief Méthode permettant de créer une propostion de line-up.
     * @param utilisateur {@link Optional<Utilisateur>}
     * @param lineUp {@link LineUp}
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    private void createValidationLineUp(Utilisateur utilisateur, LineUp lineUp) throws IllegalArgumentException {
        // On récupère les équipes des deux personnes concernées
        Equipe equipe = utilisateur.getEquipe();
        if (equipe == null) {
            throw new IllegalArgumentException("Il n'y a pas d'équipe associé à l'utilisateur avec l'id : " + utilisateur.getId());
        }
        // Récupération des membres de l'équipe de l'auteur
        List<Utilisateur> membres = utilisateurRepository.findByEquipe(equipe);
        for (Utilisateur membre : membres) {
            // On instancie un objet du modèle
            ValidationLineUp validationLineUp = new ValidationLineUp();
            validationLineUp.setLineUp(lineUp);
            validationLineUp.setUtilisateur(membre);
            validationLineUp.setStatus((Objects.equals(membre.getId(), lineUp.getAuteur().getId())) ? StatusLineUp.ACCEPTE : StatusLineUp.ATTENTE);
            // On l'enregistre dans la BDD
            validationLineUpRepository.save(validationLineUp);
        }
    }

    /**
     * @brief Méthode permettant de créer une propostion de line-up.
     * @return {@link List<LineUp>}
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    public List<LineUp> getAll() {
        return lineUpRepository.findAll();
    }

    /**
     * @brief Méthode permettant de mettre à jour
     * @param req {@link LineUpController.ValidationRequest}
     * @return message {@link String}
     * @throws IllegalArgumentException Exception si la requête est mal paramétrée
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    public String updateLineUp(LineUpController.ValidationRequest req) throws IllegalArgumentException {
        // On regarde si les arguments de la requête ne sont pas nuls
        if (req.getProposition() == null || req.getReponse() == null) {
            throw new IllegalArgumentException("Un des arguments de la requête est nul !");
        }
        // On récupère les arguments
        Optional<LineUp> lineUp = lineUpRepository.findById(req.getProposition());
        boolean reponse = req.getReponse();
        // On regarde si la line-up existe
        if (lineUp.isEmpty()) {
            throw new IllegalArgumentException("Aucune line-up n'est disponible pour cet id !");
        }
        // Si la réponse est fausse, on renvoie un message et on supprime la proposition
        if (!reponse) {
            lineUpRepository.delete(lineUp.get());
            return "La proposition de line-up a bien été rejetée !";
        } else {
            // Sinon, on va appliquer les changements proposés. D'abord, on récupère les utilisateurs concernés
            Utilisateur auteur = lineUp.get().getAuteur();
            Utilisateur proposition = lineUp.get().getProposition();
            // On récupère leurs équipes
            Equipe equipeAuteur = auteur.getEquipe();
            Equipe equipeProposition = proposition.getEquipe();
            // On vérifie que les equipes existent
            if (equipeAuteur == null || equipeProposition == null) {
                throw new IllegalArgumentException("Un des élèves à échanger n'est pas dans une équipe");
            }
            // On met à jour les id d'équipe des utilisateurs concernés
            auteur.setEquipe(equipeProposition);
            proposition.setEquipe(equipeAuteur);
            // On enregistre les modifications
            utilisateurRepository.save(auteur);
            utilisateurRepository.save(proposition);
            // on renvoit un message
            return "La proposition a bien été appliquée !";
        }
    }

    /**
     * @brief Méthode permettant de mettre à jour
     * @param auteurId {@link Integer}
     * @return lineUps {@link List<LineUp>}
     * @throws IllegalArgumentException Exception si la requête est mal paramétrée
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    public List<LineUp> getLineUpByAuteur(Integer auteurId) throws IllegalArgumentException {
        // On regarde si l'argument n'est pas nul
        if (auteurId == null) {
            throw new IllegalArgumentException("Aucun argument renseigné !");
        }
        // On regarde s'il existe un utilisateur avec cet id
        Optional<Utilisateur> auteur = utilisateurRepository.findById(auteurId);
        if (auteur.isEmpty()) {
            throw new IllegalArgumentException("Aucun utilisateur avec cet id !");
        }
        // On renvoie les line-ups
        return lineUpRepository.findByAuteur(auteur.get());
    }

    /**
     * @brief Méthode permettant de mettre à jour
     * @param req {@link LineUpController.LineUpRequest}
     * @return message {@link String}
     * @throws IllegalArgumentException Exception si la requête est mal paramétrée
     * @autor Hugo BOURDAIS
     * @date 09/06/2024
     */
    public String deleteLineUp(LineUpController.LineUpRequest req) {
        // On regarde d'abord si les arguments de la requête existent
        if (req.getAuteur() == null || req.getProposition() == null) {
            throw new IllegalArgumentException("Au moins un des arguments est absent !");
        }
        // On récupère les attributs
        Optional<Utilisateur> auteur = utilisateurRepository.findById(req.getAuteur());
        Optional<LineUp> propostion = lineUpRepository.findById(req.getProposition());
        // On regarde s'ils existent
        if (auteur.isEmpty() || propostion.isEmpty()) {
            throw new IllegalArgumentException("Au moins une des personnes rensignées n'existe pas !");
        }
        // Si l'auteur de la requête est l'auteur de la proposition alors on supprime
        if (Objects.equals(auteur.get().getId(), propostion.get().getAuteur().getId())) {
            lineUpRepository.delete(propostion.get());
            return "Proposition supprimée avec succès !";
        } else {
            throw new IllegalArgumentException("Vous ne pouvez pas supprimer une proposition dont vous n'êtes pas l'auteur !");
        }
    }
}
