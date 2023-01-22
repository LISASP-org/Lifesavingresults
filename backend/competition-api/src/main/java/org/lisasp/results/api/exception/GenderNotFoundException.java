package org.lisasp.results.api.exception;

public class GenderNotFoundException extends Exception {
    public GenderNotFoundException(String sex) {
        super(String.format("The string '%s' is no known gender.", sex));
    }
}
