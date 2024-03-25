package org.lisasp.competition.base.api.exception;

import static java.lang.String.format;

public class StorageException extends Exception {
    public StorageException(String id, Exception ex) {
        super(format("Could not access storage for id '%s'", id), ex);
    }
}
