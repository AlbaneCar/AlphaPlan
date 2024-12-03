package fr.eseo.backendalphaplan.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @file RoleType.java
 * @brief Définition de l'énumération RoleType.
 * @version 1.0
 * @author Hugo BOURDAIS & Antoine MORIN
 * @date 01/04/2024
 */
@Getter
@RequiredArgsConstructor
public enum RoleType {
    OPTION_LEADER(Set.of(
            Permission.OPTION_LEADER_UPDATE,
            Permission.OPTION_LEADER_WRITE,
            Permission.OPTION_LEADER_DELETE,
            Permission.OPTION_LEADER_READ
    )),
    PROJECT_LEADER(Set.of(
            Permission.PROJECT_LEADER_DELETE,
            Permission.PROJECT_LEADER_WRITE,
            Permission.PROJECT_LEADER_UPDATE,
            Permission.PROJECT_LEADER_READ
    )),
    SUPERVISING_STAFF(Set.of(
            Permission.SUPERVISING_STAFF_UPDATE,
            Permission.SUPERVISING_STAFF_WRITE,
            Permission.SUPERVISING_STAFF_READ,
            Permission.SUPERVISING_STAFF_DELETE
    )),
    OPTION_STUDENT(Set.of(
            Permission.OPTION_STUDENT_UPDATE,
            Permission.OPTION_STUDENT_DELETE,
            Permission.OPTION_STUDENT_WRITE,
            Permission.OPTION_STUDENT_READ
    )),
    TEAM_MEMBER(Set.of(
            Permission.TEAM_MEMBER_UPDATE,
            Permission.TEAM_MEMBER_WRITE,
            Permission.TEAM_MEMBER_READ,
            Permission.TEAM_MEMBER_DELETE
    )),
    TECHNICAL_COACHES(Set.of(
            Permission.TECHNICAL_COACHES_UPDATE,
            Permission.TECHNICAL_COACHES_READ,
            Permission.TECHNICAL_COACHES_WRITE,
            Permission.TECHNICAL_COACHES_DELETE
    )),
    JURY_MEMBER(Set.of(
            Permission.JURY_MEMBER_READ,
            Permission.JURY_MEMBER_UPDATE,
            Permission.JURY_MEMBER_WRITE,
            Permission.JURY_MEMBER_DELETE
    ));

    // Attributs
    private final Set<Permission> permissions;
    private String description;

    /**
     * @brief Méthode permettant de récupérer les permissions d'un rôle.
     * @return List<SimpleGrantedAuthority> : Liste des permissions du rôle.
     * @autor Antoine MORIN
     * @date 01/04/2024
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

    /**
     * @brief Méthode permettant de récupérer un rôle à partir d'un rôle en base de données.
     * @param roleName : String : Nom du rôle en base de données.
     * @return RoleType : Rôle correspondant au rôle en base de données.
     * @autor Antoine MORIN
     * @date 01/04/2024
     */
    public static RoleType fromDBRole(String roleName) {
        return Arrays.stream(RoleType.values())
                .filter(roleType -> roleType.name().equalsIgnoreCase(roleName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Role non trouvé"));
    }

    /**
     * @brief Méthode permettant de récupérer le nom du rôle.
     * @return String : Nom du rôle.
     * @autor Antoine MORIN
     * @date 01/04/2024
     */
    public String getNom() {
        return this.name();
    }

    /**
     * @brief Méthode permettant de récupérer la description du rôle.
     * @return String : Description du rôle.
     * @autor Antoine MORIN
     * @date 01/04/2024
     */
    public String getDescription() {
        return this.description != null ? description : null;
    }
}
