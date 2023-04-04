package org.lisasp.competition.base.api.exception;

public class CompetitionNotFoundException extends NotFoundException {
    public CompetitionNotFoundException(String id) {
        super("Competition", id);
    }
}
