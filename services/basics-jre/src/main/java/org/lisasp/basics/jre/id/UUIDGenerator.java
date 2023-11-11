package org.lisasp.basics.jre.id;

import java.util.UUID;

/**
 * Provides ids based on the UUID class.
 */
public class UUIDGenerator implements IdGenerator {

    @Override
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
