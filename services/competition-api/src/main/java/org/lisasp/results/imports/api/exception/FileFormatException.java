package org.lisasp.results.imports.api.exception;

public class FileFormatException extends Exception {
    public FileFormatException(Exception cause) {
        super("The given data does not contain a valid data format.", cause);
    }
}
