package fr.eseo.backendalphaplan.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eseo.backendalphaplan.model.BonusMalus;
import fr.eseo.backendalphaplan.repository.BonusMalusRepository;

/**
 * @file BonusMalusService.java
 * @brief Classe service pour les BonusMalus
 * @version 1.0
 * @author Enzo HERBRETEAU
 * @date 01/04/2024
 */
@Service
public class BonusMalusService {

	// Attributs
	private final BonusMalusRepository bonusMalusRepository;

	// Constructeur
	@Autowired
	public BonusMalusService(BonusMalusRepository bonusMalusRepository) {
		this.bonusMalusRepository = bonusMalusRepository;
	}

	/**
	 * @brief Méthode pour sauvegarder un BonusMalus
	 * @param bm : BonusMalus à sauvegarder
	 * @return BonusMalus sauvegardé
	 */
	public BonusMalus saveBonusMalus(BonusMalus bm) {
		return bonusMalusRepository.save(bm);
	}

	/**
	 * @brief Méthode pour un BonusMalus via son id
	 * @param id : id du BonusMalus
	 * @return Liste des BonusMalus
	 */
	public BonusMalus getBonusMalusById(Integer id) {
		return bonusMalusRepository.findById(id).orElse(null);
	}

	/**
	 * @brief Méthode pour récupérer un BonusMalus via l'id de la note
	 * @param id : id de la note
	 * @return Liste des BonusMalus
	 */
	public BonusMalus getBonusMalusByNoteId(Integer id) {
		return bonusMalusRepository.findByNoteId(id);
	}

	/**
	 * @brief Méthode pour récupérer les BonusMalus d'une équipe pour un sprint donné
	 * @param id : id de l'équipe
	 * @param sprint : numéro du sprint
	 * @return Liste des BonusMalus
	 */
	public List<BonusMalus> getBonusMalusSprint(Integer id, int sprint) {
		return bonusMalusRepository.getBonusMalusSprint(id, sprint);
	}

	/**
	 * @brief Méthode pour supprimer un BonusMalus via son id
	 * @param id : id de l'équipe
	 */
	public void deleteBMbyId(Integer id) {
		bonusMalusRepository.deleteById(id);
	}
}
