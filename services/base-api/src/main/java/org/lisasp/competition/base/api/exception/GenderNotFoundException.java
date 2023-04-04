package org.lisasp.competition.base.api.exception;

public class GenderNotFoundException extends Exception {
    public GenderNotFoundException(String unknownGender) {
        super(String.format("The string '%s' is no known gender.", unknownGender));
    }
}
