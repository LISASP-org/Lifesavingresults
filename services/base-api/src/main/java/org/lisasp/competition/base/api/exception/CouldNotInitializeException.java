package org.lisasp.competition.base.api.exception;

public class CouldNotInitializeException extends Exception {

    public CouldNotInitializeException(String type, Exception ex) {
        super(String.format("Could not initialize '%s'.", type), ex);
    }
}
