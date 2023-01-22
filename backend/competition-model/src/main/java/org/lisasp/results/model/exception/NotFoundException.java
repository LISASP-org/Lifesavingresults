package org.lisasp.results.model.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String type, String id) {
        super(String.format("%s with %s not found.", type, id));
    }
}
