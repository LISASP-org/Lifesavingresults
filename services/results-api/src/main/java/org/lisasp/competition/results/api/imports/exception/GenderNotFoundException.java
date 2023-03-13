package org.lisasp.competition.results.api.imports.exception;

public class GenderNotFoundException extends Exception {
    public GenderNotFoundException(String unkownGender) {
        super(String.format("The string '%s' is no known gender.", unkownGender));
    }
}
