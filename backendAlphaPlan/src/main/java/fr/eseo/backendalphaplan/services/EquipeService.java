package fr.eseo.backendalphaplan.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.EtatEquipes;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;
import fr.eseo.backendalphaplan.model.enums.Genre;
import fr.eseo.backendalphaplan.repository.EquipeRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;

import java.io.IOException;
import java.util.*;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @file EquipeService.java
 * @brief Définition de la classe EquipeService.
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
public class EquipeService {

    // Constantes
    private static final Logger logger = LoggerFactory.getLogger(EquipeService.class);
    private static final String ERROR_MESSAGE = "An error occurred: ";

    // Attributs
    private final EquipeRepository equipeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurService utilisateurService;
    private final NoteAnterieureService noteAnterieureService;
    private final EntityManager entityManager;

    // Constructeur
    @Autowired
    public EquipeService(UtilisateurService utilisateurService, UtilisateurRepository utilisateurRepository, NoteAnterieureService noteAnterieureService, EquipeRepository equipeRepository, EntityManager entityManager) {
        this.utilisateurService = utilisateurService;
        this.noteAnterieureService = noteAnterieureService;
        this.equipeRepository = equipeRepository;
        this.entityManager = entityManager;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * @param id : Integer : Id de l'équipe à récupérer.
     * @return Equipe : Equipe récupérée.
     * @brief Méthode permettant de récupérer une équipe par son id.
     * @author Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public Equipe getEquipeById(Integer id) {
        // Récupération de l'équipe par son id
        return equipeRepository.findById(id).orElse(null);
    }

    /**
     * @param nom : String : Nom de l'équipe à récupérer.
     * @return Equipe : Equipe récupérée.
     * @brief Méthode permettant de récupérer une équipe par son nom.
     */

    public Equipe getEquipeReferentById(Integer id) {
    	Utilisateur user = utilisateurRepository.findById(id).orElse(null);
    	Equipe team = new Equipe();
    	if (user != null) {
        	team = equipeRepository.findByUtilisateur(user);
    	}
    	return team;
    }

    public Equipe getEquipeByNom(String nom) {
        // Récupération de l'équipe par son id
        return equipeRepository.findByNom(nom);
    }

    /**
     * @return List<Equipe> : Liste des équipes.
     * @brief Méthode permettant de récupérer toutes les équipes.
     */
    public List<Equipe> getAllEquipes() {
        return equipeRepository.findAll();
    }

    /**
     * @param equipe : Equipe : Equipe à sauvegarder.
     * @return Equipe : Equipe sauvegardée.
     * @brief Méthode permettant de sauvegarder une équipe.
     */
    public Equipe saveEquipe(Equipe equipe) {
        return equipeRepository.save(equipe);
    }

    /**
     * @brief Méthode permettant de supprimer toutes les équipes.
     */
    public void deleteEquipe() {
        equipeRepository.deleteAll();
    }

    /**
     * @brief Méthode permettant de récupérer toutes les équipes.
     */
    public List<Equipe> getEquipes() {
        return equipeRepository.findAll();
    }

    /**
     * @param id : Integer : Id de l'équipe.
     * @return List<Utilisateur> : Liste des étudiants de l'équipe.
     * @brief Méthode permettant de récupérer les étudiants d'une équipe par son id.
     */
    public List<Utilisateur> getStudentsByEquipeId(Integer id) {
        return equipeRepository.getStudentsByEquipeId(id);
    }

    /**
     * @param nbEquipes : Integer : Nombre d'équipes à créer.
     * @return List<Utilisateur> : Liste des utilisateurs de réordonnés
     * @brief Méthode permettant de créer les équipes.
     */
    public List<List<Utilisateur>> creerEquipes(Integer nbEquipes) {
        List<Utilisateur> eleves = utilisateurService.getEleves();

        // Création des listes pour les équipes, les femmes et les bachelors
        List<Utilisateur> femmes = new ArrayList<>();
        List<Utilisateur> bachelor = new ArrayList<>();
        List<Utilisateur> autres = new ArrayList<>();

        // Répartir les étudiants en femmes, bachelor et autres
        for (Utilisateur eleve : eleves) {
            if (Genre.FEMME.equals(eleve.getGenre())) {
                femmes.add(eleve);
            } else if (TypeUtilisateur.BACHELOR.equals(eleve.getTypeUtilisateur())) {
                bachelor.add(eleve);
            } else {
                autres.add(eleve);
            }
        }

        // Trier les femmes par moyenne décroissante
        femmes.sort(Comparator.comparingDouble(eleve -> noteAnterieureService.getMoyenneUtilisateur(eleve.getId())));
        Collections.reverse(femmes);

        // Trier les bachelors par moyenne croissante
        bachelor.sort(Comparator.comparingDouble(eleve -> noteAnterieureService.getMoyenneUtilisateur(eleve.getId())));

        // Trier les autres étudiants par moyenne
        autres.sort(Comparator.comparingDouble(eleve -> noteAnterieureService.getMoyenneUtilisateur(eleve.getId())));

        // Créer la liste de tous les étudiants dans l'ordre des moyennes
        List<Utilisateur> tousEleves = new ArrayList<>();
        tousEleves.addAll(femmes);
        tousEleves.addAll(bachelor);
        tousEleves.addAll(autres);

        // Initialisation des équipes
        List<List<Utilisateur>> equipes = new ArrayList<>();
        for (int i = 0; i < nbEquipes; i++) {
            equipes.add(new ArrayList<>());
        }

        // Répartition équitable des étudiants dans les équipes
        for (int i = 0; i < tousEleves.size(); i++) {
            Utilisateur eleve = tousEleves.get(i);
            int equipeIndex = i % nbEquipes;
            equipes.get(equipeIndex).add(eleve);
        }

        // Calcul des moyennes des équipes
        List<Double> moyennesEquipes = new ArrayList<>();
        for (List<Utilisateur> equipe : equipes) {
            double moyenneEquipe = equipe.stream()
                    .mapToDouble(eleve -> noteAnterieureService.getMoyenneUtilisateur(eleve.getId()))
                    .average()
                    .orElse(0.0);
            moyennesEquipes.add(moyenneEquipe);
        }

        // Tri des équipes par moyenne
        equipes.sort(Comparator.comparingDouble((List<Utilisateur> equipe) -> equipe.stream()
                .mapToDouble(eleve -> noteAnterieureService.getMoyenneUtilisateur(eleve.getId()))
                .average()
                .orElse(0.0)));

        return equipes;
    }

    /**
     * @param json : String : JSON contenant les équipes.
     * @param etatEquipes : EtatEquipes : État des équipes.
     * @brief Méthode permettant de traiter le JSON et d'assigner les utilisateurs aux équipes.
     */

    @Transactional
    public void processJsonAndAssignUsers(String json, EtatEquipes etatEquipes) {
        try {
            // Étape 1 : Supprimer toutes les équipes existantes
            removeAllEquipes();

            // Étape 2 : Ajouter les nouvelles équipes à partir du JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);

            for (JsonNode equipeNode : jsonNode) {
                String equipeName = null;
                JsonNode equipeStaff = null;
                if (etatEquipes != EtatEquipes.GENERE) {
                    equipeName = equipeNode.get("name").asText();
                    equipeStaff = equipeNode.get("staff");
                }
                JsonNode membersNode = equipeNode.get("members");

                Integer staffId = null;
                if (etatEquipes != EtatEquipes.GENERE) {
                    staffId = equipeStaff.get("id").asInt();
                }

                // Créer une nouvelle équipe
                Equipe equipe = new Equipe();
                if (etatEquipes != EtatEquipes.GENERE){
                    equipe.setNom(equipeName);
                    equipe.setUtilisateur(utilisateurService.getUtilisateurById(staffId));
                }
                equipe.setEtatEquipes(etatEquipes);
                entityManager.persist(equipe);

                // Parcourir les membres de l'équipe et les assigner
                for (JsonNode memberNode : membersNode) {
                    int userId = memberNode.get("id").asInt();
                    assignerUtilisateurAEquipe(userId, equipe.getId());
                }
            }

            // Valider la transaction
            entityManager.flush(); // Forcer l'exécution des opérations en attente
        } catch (IOException e) {
            logger.error( ERROR_MESSAGE , e);
        }
    }

    /**
     * @brief Méthode permettant de supprimer toutes les équipes.
     */

    @Transactional
    public void removeAllEquipes() {
        try {
            // Récupérer toutes les équipes
            List<Equipe> equipes = entityManager.createQuery("SELECT e FROM Equipe e", Equipe.class).getResultList();

            // Parcourir chaque équipe
            for (Equipe equipe : equipes) {
                // Dissocier les utilisateurs de cette équipe
                dissociateUtilisateursFromEquipe(equipe);

                // Supprimer l'équipe
                entityManager.remove(equipe);
            }

            // Valider la transaction
            entityManager.flush(); // Forcer l'exécution des opérations en attente
        } catch (Exception e) {
            logger.error( ERROR_MESSAGE , e);
        }
    }

    /**
     * @param equipe : Equipe : Équipe à laquelle les utilisateurs doivent être dissociés.
     * @brief Méthode permettant de dissocier les utilisateurs d'une équipe.
     */

    @Transactional
    public void dissociateUtilisateursFromEquipe(Equipe equipe) {
        try {
            // Récupérer tous les utilisateurs associés à cette équipe
            List<Utilisateur> utilisateurs = entityManager.createQuery("SELECT u FROM Utilisateur u WHERE u.equipe = :equipe", Utilisateur.class)
                    .setParameter("equipe", equipe)
                    .getResultList();

            // Dissocier chaque utilisateur de cette équipe
            for (Utilisateur utilisateur : utilisateurs) {
                utilisateur.setEquipe(null); // Dissocier l'utilisateur de l'équipe
                entityManager.merge(utilisateur); // Mettre à jour l'utilisateur dans la base de données
            }
        } catch (Exception e) {
            logger.error( ERROR_MESSAGE , e);
        }
    }

    /**
     * @param utilisateurId : Integer : Id de l'utilisateur.
     * @param equipeId : Integer : Id de l'équipe.
     * @brief Méthode permettant d'assigner un utilisateur à une équipe.
     */
    @Transactional
    public void assignerUtilisateurAEquipe(Integer utilisateurId, Integer equipeId) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(utilisateurId);
        Equipe equipe = entityManager.find(Equipe.class, equipeId);

        if (utilisateur != null && equipe != null) {
            utilisateur.setEquipe(equipe);
            entityManager.merge(utilisateur); // Met à jour l'utilisateur avec la nouvelle équipe associée
        }
    }

    /**
     * @param etatEquipes : EtatEquipes : État des équipes.
     * @brief Méthode permettant de changer l'état des équipes.
     */
    @Transactional
    public void changeTeamsState(EtatEquipes etatEquipes) {
        try {
            List<Equipe> equipes = equipeRepository.findAll();
            for (Equipe equipe : equipes) {
                equipe.setEtatEquipes(etatEquipes);
                entityManager.merge(equipe);
            }
            entityManager.flush();
        } catch (Exception e) {
            logger.error(ERROR_MESSAGE, e);
        }
    }
}


