package fr.eseo.backendalphaplan.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.eseo.backendalphaplan.model.BonusMalus;
import fr.eseo.backendalphaplan.services.BonusMalusService;

/**
 * @file BonusMalusController.java
 * @brief La classe BonusMalusController est le controller qui permet de gérer les requêtes HTTP concernant les bonus/malus
 * @version 1.0
 * @date 24/04/2024
 * @autor Yann LIDEC
 */
@RestController
@RequestMapping("/api/v1.0/bonusMalus")
public class BonusMalusController {

    // Attributs
    private BonusMalusService bonusMalusService;

    // Constructeur
    @Autowired
    public BonusMalusController(BonusMalusService bonusMalusService) {
        this.bonusMalusService = bonusMalusService;
    }
    
    /**
     * @brief Méthode permettant de récupérer tous les bonus malus associés à une équipe
     * @param id : id de l'équipe / RequestParam sprint : sprint voulu
     * @param sprint : numéro du sprint
     * @return Liste des notes de l'équipe
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @GetMapping("/{id}/liste")
    public List<BonusMalus> getBonusMalus(@PathVariable Integer id, @RequestParam int sprint) {
        return bonusMalusService.getBonusMalusSprint(id, sprint);
    }
    
    /**
     * @brief Méthode permettant de récupérer un BM par son id
     * @param id : id du BM recherché
     * @return Le BonusMalus en question
     * @autor Yann LIDEC
     * @date 03/05/2024
     */
    @GetMapping("/{id}")
    public BonusMalus getBonusMalusById(@PathVariable Integer id) {
        return bonusMalusService.getBonusMalusById(id);
    }
    
    /**
     * @brief Méthode permettant de récupérer un BM par l'id de la note associée
     * @param id : id de la note recherchée
     * @return Le BonusMalus en question
     * @autor Yann LIDEC
     * @date 04/05/2024
     */
    @GetMapping("/note/{id}")
    public BonusMalus getBonusMalusByNoteId(@PathVariable Integer id) {
        return bonusMalusService.getBonusMalusByNoteId(id);
    }
    
    /**
     * @brief Méthode d'ajouter un bonus/malus à une note
     * @param bonusMalus : Bonus/Malus à ajouter
     * @return Validation de l'insertion du Bonus/Malus
     * @autor Yann LIDEC
     * @date 23/04/2024
     */
    @PostMapping("/ajouter")
    public ResponseEntity<BonusMalus> ajouterBM(@RequestBody BonusMalus bonusMalus) {
    	BonusMalus bm = bonusMalusService.saveBonusMalus(bonusMalus);
        return ResponseEntity.status(HttpStatus.CREATED).body(bm);
    }
    
    /**
     * @brief Méthode de supprimer un bonus/malus
     * @param id d'un BM
     * @return Validation de la suppression du Bonus/Malus
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<String> supprimerBM(@PathVariable int id) {
        try {
        	bonusMalusService.deleteBMbyId(id);
            return ResponseEntity.ok().body("BM supprimé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur lors de la suppression du BM.");
        }
    }

    /**
     * @brief Méthode de modifier un bonus/malus
     * @param id : id du BM à modifier
     * @param requestBody : requête contenant les modifications
     * @return Validation de la modification du Bonus/Malus
     * @autor Yann LIDEC
     * @date 24/04/2024
     */
    @PatchMapping("/{id}")
    public ResponseEntity<BonusMalus> modifierBonusMalus(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody){
        BonusMalus bm = bonusMalusService.getBonusMalusById(id);
          if (bm != null) {
              //Modifier la note
              if (requestBody.containsKey("valide")) {
                  Boolean noteNumber = (Boolean) requestBody.get("valide");
                  bm.setIsValide(noteNumber);
              }
              bonusMalusService.saveBonusMalus(bm);
              return ResponseEntity.ok(bm);
          }else{
              return ResponseEntity.notFound().build();
          }
    }
}
