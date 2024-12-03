package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.controller.OrdrePassageController.OrdrePassageResponse;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.OrdrePassage;
import fr.eseo.backendalphaplan.model.Sprint;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.repository.EquipeRepository;
import fr.eseo.backendalphaplan.repository.OrdrePassageRepository;
import fr.eseo.backendalphaplan.repository.SprintRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @file OrdrePassageService.java
 * @brief Service permettant de gérer les ordres de passage
 * @details Ce service permet de récupérer un ordre de passage en fonction de l'id de l'équipe et de l'id du sprint, et de créer un ordre de passage
 * @version 1.0
 * @author Hugo BOURDAIS
 * @date 24/05/2024
 */
@Service
public class OrdrePassageService {

    // Attributs
    private final OrdrePassageRepository ordrePassageRepository;
    private final EquipeRepository equipeRepository;
    private final SprintRepository sprintRepository;
    private final UtilisateurRepository utilisateurRepository;

    // Constructeur
    @Autowired
    public OrdrePassageService(OrdrePassageRepository ordrePassageRepository, EquipeRepository equipeRepository, SprintRepository sprintRepository, UtilisateurRepository utilisateurRepository) {
        this.ordrePassageRepository = ordrePassageRepository;
        this.equipeRepository = equipeRepository;
        this.sprintRepository = sprintRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * @brief Récupère l'ordre de passage en fonction de l'id de l'équipe et de l'id du sprint
     * @param sprintId l'id du sprint
     * @param equipeId l'id de l'équipe
     * @return l'ordre de passage
     * @throws IllegalArgumentException si l'id du sprint ou l'id de l'équipe ne sont pas renseignés, si l'équipe n'existe pas, si le sprint n'existe pas ou si l'ordre de passage n'existe pas
     * @author Hugo BOURDAIS
     * @date 24/05/2024
     */
    public OrdrePassageResponse getOrdrePassageByEquipeIdAndSprintId(Integer sprintId, Integer equipeId) throws IllegalArgumentException {
        // On vérifie que l'id du sprint et l'id de l'équipe sont bien renseignés
        if (sprintId == null || equipeId == null) {
            throw new IllegalArgumentException("L'id du sprint et l'id de l'équipe doivent être renseignés");
        }

        // On vérifie que l'équipe existe
        if (!equipeRepository.existsById(equipeId)) {
            throw new IllegalArgumentException("L'équipe n'existe pas");
        }

        // On vérifie que le sprint existe
        if (!sprintRepository.existsById(sprintId)) {
            throw new IllegalArgumentException("Le sprint n'existe pas");
        }

        // On récupère l'ordre de passage en fonction de l'id de l'équipe et de l'id du sprint
        OrdrePassage ordrePassage = ordrePassageRepository.findByEquipeIdAndSprintId(equipeId, sprintId);

        // On vérifie que l'ordre de passage existe
        if (ordrePassage == null) {
            throw new IllegalArgumentException("L'ordre de passage n'existe pas");
        }

        // On retourne l'ordre de passage
        return OrdrePassageResponse.builder()
                .id(ordrePassage.getId())
                .equipe(ordrePassage.getEquipe())
                .sprint(ordrePassage.getSprint())
                .auteur(ordrePassage.getAuteur())
                .dateCreation(String.valueOf(ordrePassage.getDateCreation()))
                .ordre(ordrePassage.getOrdre())
                .build();
    }

    /**
     * @brief  Crée un ordre de passage en fonction de l'id du sprint, de l'id de l'équipe et de l'ordre
     * @param sprintId l'id du sprint
     * @param equipeId l'id de l'équipe
     * @param ordre l'ordre
     */
    public void createOrdrePassage(Integer sprintId, Integer equipeId, List<Integer> ordre, Integer auteurId) {
        // On vérifie que les champs sont bien renseignés
        if (!areParamValid(sprintId, equipeId, ordre, auteurId)) {
            throw new IllegalArgumentException("Les champs doivent être renseignés");
        }

        // On récupère les données
        Utilisateur auteur = utilisateurRepository.findById(auteurId).orElse(null);
        Sprint sprint = sprintRepository.findById(sprintId).orElse(null);
        Equipe equipe = equipeRepository.findById(equipeId).orElse(null);
        List<Utilisateur> utilisateurs = new ArrayList<>();
        for (Integer id : ordre) {
            utilisateurs.add(utilisateurRepository.findById(id).orElse(null));
        }

        // On vérifie que les éléments existent
        if (!doElementsExist(sprint, equipe, auteur, utilisateurs)) {
            throw new IllegalArgumentException("Les éléments n'existent pas");
        }

        // On essaie de récupérer l'ordre de passage pour voir s'il existe déjà
        OrdrePassage ordrePassageExist = ordrePassageRepository.findByEquipeIdAndSprintId(equipeId, sprintId);
        if (ordrePassageExist != null) {
            // On supprime l'ancien ordre de passage s'il existe
            ordrePassageRepository.delete(ordrePassageExist);
        }

        // On crée l'ordre de passage
        OrdrePassage ordrePassage = new OrdrePassage();
        ordrePassage.setEquipe(equipe);
        ordrePassage.setSprint(sprint);
        ordrePassage.setAuteur(auteur);
        ordrePassage.setOrdre(utilisateurs);

        // On sauvegarde l'ordre de passage
        ordrePassageRepository.save(ordrePassage);
    }

    /**
     * @brief Vérifie que les paramètres sont valides
     * @param sprintId l'id du sprint
     * @param equipeId l'id de l'équipe
     * @param ordre l'ordre
     * @param auteurId l'id de l'auteur
     * @return true si les paramètres sont valides, false sinon
     * @date 24/05/2024
     * @author Hugo BOURDAIS
     */
    private boolean areParamValid(Integer sprintId, Integer equipeId, List<Integer> ordre, Integer auteurId) {
        return sprintId != null && equipeId != null && ordre != null && auteurId != null;
    }

    /**
     * @brief Vérifie que les éléments existent
     * @param sprint le sprint
     * @param equipe l'équipe
     * @param auteur l'auteur
     * @param utilisateurs les utilisateurs
     * @return true si les éléments existent, false sinon
     * @date 24/05/2024
     * @author Hugo BOURDAIS
     */
    private boolean doElementsExist(Sprint sprint, Equipe equipe, Utilisateur auteur, List<Utilisateur> utilisateurs) {
        return sprint != null && equipe != null && auteur != null && utilisateurs.stream().allMatch(Objects::nonNull);
    }
}
