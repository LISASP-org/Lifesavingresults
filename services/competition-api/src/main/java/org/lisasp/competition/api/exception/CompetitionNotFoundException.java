package org.lisasp.competition.api.exception;

public class CompetitionNotFoundException extends NotFoundException {
    public CompetitionNotFoundException(String id) {
        super("Competition", id);
    }
}
