package fr.eseo.backendalphaplan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.eseo.backendalphaplan.model.enums.Genre;
import fr.eseo.backendalphaplan.model.enums.RoleType;
import fr.eseo.backendalphaplan.model.enums.TypeUtilisateur;

import jakarta.persistence.*;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * @file Utilisateur.java
 * @brief Classe permettant de définir un utilisateur qui étend UserDetails de Spring Security
 * @version 1.0
 * @author Enzo HERBRETEAU && Antoine MORIN
 * @date 01/04/2024
 */
@Entity
@Data
@NamedQuery(name = "Utilisateur.findAll", query = "SELECT u FROM Utilisateur u")
@RequiredArgsConstructor
@AllArgsConstructor
public class Utilisateur implements UserDetails {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    private String prenom;

    private String email;

	@JsonIgnore
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private Genre genre;
    
    @Enumerated(EnumType.STRING)
    private TypeUtilisateur typeUtilisateur;

    @ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "equipe_id", referencedColumnName="id")
    private Equipe equipe;

	@MapsId("UtilisateurRoleId")
	@OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<RoleUtilisateur> roles;

	/**
	 * @brief Méthode permettant de récupérer les rôles de l'utilisateur
	 * @return Collection<? extends GrantedAuthority> : Collection des rôles de l'utilisateur
	 * @autor Antoine MORIN
	 * @date 17/04/2024
	 * @see RoleType
	 */
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		for(RoleUtilisateur roleUtilisateur : roles) {
			String roleName = roleUtilisateur.getRole().getNom();
			RoleType roleType = RoleType.fromDBRole(roleName);
			authorities.addAll(roleType.getAuthorities());
		}
		return authorities;
	}

	/**
	 * @brief Méthode permettant de récupérer le mot de passe de l'utilisateur
	 * @return String : Mot de passe de l'utilisateur
	 * @autor Antoine MORIN
	 * @date 17/04/2024
	 */
	@Override
	@JsonIgnore
	public String getPassword() {
		return this.motDePasse;
	}

	/**
	 * @brief Méthode permettant de récupérer l'identifiant de l'utilisateur
	 * @return String : Identifiant de l'utilisateur
	 * @autor Antoine MORIN
	 * @date 17/04/2024
	 */
	@Override
	@JsonIgnore
	public String getUsername() {
		return this.email;
	}

	/**
	 * @brief Méthode permettant de vérifier si le compte de l'utilisateur est expiré
	 * @return boolean : Vrai si le compte n'est pas expiré, faux sinon
	 * @autor Antoine MORIN
	 * @date 17/04/2024
	 */
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * @brief Méthode permettant de vérifier si le compte de l'utilisateur est verrouillé
	 * @return boolean : Vrai si le compte n'est pas verrouillé, faux sinon
	 * @autor Antoine MORIN
	 * @date 17/04/2024
	 */
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * @brief Méthode permettant de vérifier si les informations de l'utilisateur sont expirées
	 * @return boolean : Vrai si les informations ne sont pas expirées, faux sinon
	 * @autor Antoine MORIN
	 * @date 17/04/2024
	 */
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * @brief Méthode permettant de vérifier si l'utilisateur est activé
	 * @return boolean : Vrai si l'utilisateur est activé, faux sinon
	 * @autor Antoine MORIN
	 * @date 17/04/2024
	 */
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
}