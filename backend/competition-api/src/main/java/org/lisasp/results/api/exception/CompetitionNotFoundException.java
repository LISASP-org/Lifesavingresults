package org.lisasp.results.api.exception;

public class CompetitionNotFoundException extends Exception {
    public CompetitionNotFoundException(String id) {
        super(String.format("Competition with id '%s' not found.", id));
    }
}
