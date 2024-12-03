package fr.eseo.backendalphaplan.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @file Permission.java
 * @brief Définition de l'énumération Permission.
 * @version 1.0
 * @author Hugo BOURDAIS & Antoine MORIN
 * @date 01/04/2024
 */
@Getter
@RequiredArgsConstructor
public enum Permission {

    OPTION_LEADER_READ("optionLeader:read"),
    OPTION_LEADER_WRITE("optionLeader:write"),
    OPTION_LEADER_DELETE("optionLeader:delete"),
    OPTION_LEADER_UPDATE("optionLeader:update"),

    PROJECT_LEADER_READ("projectLeader:read"),
    PROJECT_LEADER_WRITE("projectLeader:write"),
    PROJECT_LEADER_DELETE("projectLeader:delete"),
    PROJECT_LEADER_UPDATE("projectLeader:update"),

    SUPERVISING_STAFF_READ("supervisingStaff:read"),
    SUPERVISING_STAFF_WRITE("supervisingStaff:write"),
    SUPERVISING_STAFF_DELETE("supervisingStaff:delete"),
    SUPERVISING_STAFF_UPDATE("supervisingStaff:update"),

    OPTION_STUDENT_READ("optionStudent:read"),
    OPTION_STUDENT_WRITE("optionStudent:write"),
    OPTION_STUDENT_DELETE("optionStudent:delete"),
    OPTION_STUDENT_UPDATE("optionStudent:update"),

    TEAM_MEMBER_READ("teamMember:read"),
    TEAM_MEMBER_WRITE("teamMember:write"),
    TEAM_MEMBER_DELETE("teamMember:delete"),
    TEAM_MEMBER_UPDATE("teamMember:update"),

    TECHNICAL_COACHES_READ("technicalCoaches:read"),
    TECHNICAL_COACHES_WRITE("technicalCoaches:write"),
    TECHNICAL_COACHES_DELETE("technicalCoaches:delete"),
    TECHNICAL_COACHES_UPDATE("technicalCoaches:update"),

    JURY_MEMBER_READ("juryMember:read"),
    JURY_MEMBER_WRITE("juryMember:write"),
    JURY_MEMBER_DELETE("juryMember:delete"),
    JURY_MEMBER_UPDATE("juryMember:update");

    // Attributs
    private final String permission;
}
