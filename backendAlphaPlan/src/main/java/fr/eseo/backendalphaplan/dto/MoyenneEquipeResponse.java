package fr.eseo.backendalphaplan.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MoyenneEquipeResponse(
        Integer idEquipe,
        Integer idSprint,
        Double moyenne,
        LocalDate date
) {
}
