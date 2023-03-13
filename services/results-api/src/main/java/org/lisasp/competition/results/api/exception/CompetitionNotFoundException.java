package org.lisasp.competition.results.api.exception;

public class CompetitionNotFoundException extends NotFoundException {
    public CompetitionNotFoundException(String id) {
        super("Competition", id);
    }
}
