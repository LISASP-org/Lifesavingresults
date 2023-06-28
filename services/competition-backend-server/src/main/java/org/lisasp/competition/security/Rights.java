package org.lisasp.competition.security;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.authorization.Right;

import java.util.Locale;

@RequiredArgsConstructor
public enum Rights {
    CompetitionEdit(Modules.Competition, "edit"),
    CompetitionDelete(Modules.Competition, "delete"),
    ResultsImport(Modules.Results, "import"),
    AuthorizationCompetition(Modules.Authorization, "competition");

    private final Modules module;
    private final String name;

    public Right toAction() {
        return Right.builder().module(module.name().toLowerCase(Locale.ROOT)).name(name.trim().toLowerCase()).build();
    }
}
