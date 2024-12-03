package fr.eseo.backendalphaplan.services;

import fr.eseo.backendalphaplan.model.Equipe;
import fr.eseo.backendalphaplan.model.NoteAnterieure;
import fr.eseo.backendalphaplan.model.RoleUtilisateur;
import fr.eseo.backendalphaplan.model.Utilisateur;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;
import fr.eseo.backendalphaplan.repository.NoteAnterieureRepository;
import fr.eseo.backendalphaplan.repository.RoleUtilisateurRepository;
import fr.eseo.backendalphaplan.repository.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @file UtilisateurService.java
 * @brief Définition de la classe UtilisateurService.
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
public class UtilisateurService {

    // Instance de UtilisateurRepository
    private final UtilisateurRepository utilisateurRepository;
    private final NoteAnterieureRepository noteAnterieureRepository;
    private final RoleUtilisateurRepository roleUtilisateurRepository;

    // Constructeur
    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, NoteAnterieureRepository noteAnterieureRepository, RoleUtilisateurRepository roleUtilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.noteAnterieureRepository = noteAnterieureRepository;
        this.roleUtilisateurRepository = roleUtilisateurRepository;
    }

    /**
     * @brief Méthode permettant de récupérer un utilisateur par son id.
     * @param id : Integer : Id de l'utilisateur à récupérer.
     * @return Utilisateur : Utilisateur récupéré.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public Utilisateur getUtilisateurById(Integer id) {
        // Retourne l'utilisateur correspondant à l'id passé en paramètre
        return utilisateurRepository.findById(id).orElse(null);
    }

    /**
     * @brief Méthode permettant de récupérer un utilisateur par son email.
     * @param email : String : Email de l'utilisateur à récupérer.
     * @return Utilisateur : Utilisateur récupéré.
     * @autor Hugo BOURDAIS
     * @date 01/04/2024
     */
    public Utilisateur getUtilisateurByEmail(String email) {
        // Retourne l'utilisateur correspondant à l'email passé en paramètre
        return utilisateurRepository.findByEmail(email);
    }

    /**
     * @brief Méthode permettant de récupérer tous les utilisateurs.
     * @return List<Utilisateur> : Liste des utilisateurs.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public List<Utilisateur> getUtilisateurs(){
        // Retourne la liste de tous les utilisateurs
        return utilisateurRepository.findAll();
    }

    /**
     * @brief Méthode permettant de récupérer un utilisateur par son nom et prénom.
     * @param nom : String : Nom de l'utilisateur à récupérer.
     * @param prenom : String : Prénom de l'utilisateur à récupérer.
     * @return Utilisateur : Utilisateur récupéré.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public Utilisateur findByNomAndPrenom(String nom, String prenom) {
        return utilisateurRepository.findNomAndPrenom(nom, prenom);
    }

    /**
     * @brief Méthode permettant de supprimer un utilisateur.
     * @param utilisateur : Utilisateur : Utilisateur à supprimer.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public void deleteUtilisateur(Utilisateur utilisateur) {
        utilisateurRepository.delete(utilisateur);
    }


    /**
     * @brief Méthode permettant de récupérer les utilisateurs encadrants.
     * @return List<Utilisateur> : Liste des utilisateurs encadrants.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public List<Utilisateur> getUtilisateursEncadrant() {
        return utilisateurRepository.findEncadrant(TypeUtilisateur.ENCADRANT);
    }

    /**
     * @brief Méthode permettant de récupérer les utilisateurs élèves.
     * @return List<Utilisateur> : Liste des utilisateurs élèves.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public List<Utilisateur> getEleves() {
        return utilisateurRepository.findEleve(TypeUtilisateur.E3E, TypeUtilisateur.BACHELOR);
    }

    /**
     * @brief Méthode permettant de sauvegarder un utilisateur.
     * @param utilisateur : Utilisateur : Utilisateur à sauvegarder.
     * @return Utilisateur : Utilisateur sauvegardé.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        // Retourne l'utilisateur sauvegardé
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * @brief Méthode permettant de récupérer les utilisateurs d'une équipe.
     * @param equipe : Equipe : Equipe des utilisateurs à récupérer.
     * @return List<Utilisateur> : Liste des utilisateurs de l'équipe.
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public List<Utilisateur> getUtilisateursByEquipe(Equipe equipe) {
        return utilisateurRepository.findByEquipe(equipe);
    }

    /**
     * @brief Méthode permettant de récupérer les utilisateurs
     * @param pageable : Pageable : Pageable
     * @return Page<Utilisateur> : Page des utilisateurs
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public Page<Utilisateur> getStudents(Pageable pageable) {
        return utilisateurRepository.findAllE3EOrBachelorUsers(pageable);
    }

    /**
     * @brief Méthode permettant de sauvegarder une note antérieure
     * @param noteAnterieure : NoteAnterieure : Note antérieure à sauvegarder
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public void saveNoteAnterieure(NoteAnterieure noteAnterieure) {
        noteAnterieureRepository.save(noteAnterieure);
    }

    /**
     * @brief Méthode permettant de supprimer une note antérieure par utilisateur
     * @param utilisateurExistant : Utilisateur : Utilisateur dont on veut supprimer les notes antérieures
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public void deleteNoteAnterieureByUtilisateur(Utilisateur utilisateurExistant) {
        List<NoteAnterieure> notesAnterieures = noteAnterieureRepository.findByUtilisateur(utilisateurExistant);
        noteAnterieureRepository.deleteAll(notesAnterieures);
    }

    /**
     * @brief Méthode permettant de supprimer un utilisateur par son id
     * @param id : Integer : Id de l'utilisateur à supprimer
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public void deleteUserById(Integer id) {
        utilisateurRepository.deleteById(id);
    }

    /**
     * @brief Méthode permettant de récupérer les encadrants
     * @return List<Utilisateur> : Liste des encadrants
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public List<Utilisateur> getEncadrants(){
        return utilisateurRepository.findEncadrants();
    }

    /**
     * @brief Méthode permettant de récupérer les roles d'un utilisateur donné
     * @param utilisateur : Utilisateur : Utilisateur dont on veut récupérer les roles
     * @return List<RoleUtilisateur> : Liste des roles de l'utilisateur
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public List<RoleUtilisateur> getRoles(Utilisateur utilisateur) {
        return roleUtilisateurRepository.findAllByUtilisateurId(utilisateur.getId());
    }

    /**
     * @brief Méthode permettant de récupérer les utilisateurs OL et PL
     * @return List<Utilisateur> : Liste des utilisateurs OL et PL
     * @autor Enzo HERBRETEAU
     * @date 01/04/2024
     */
    public List<Utilisateur> getOLPL(){
        return utilisateurRepository.findOLPL();
    }
}
