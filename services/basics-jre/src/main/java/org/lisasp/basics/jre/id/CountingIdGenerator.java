package org.lisasp.basics.jre.id;

import lombok.Synchronized;

/**
 * CountingIdGenerator provides integer based ids in ascending order. This class is thread safe.
 */
public class CountingIdGenerator implements IdGenerator {

    private volatile int key = 0;

    @Synchronized
    @Override
    public String nextId() {
        key++;
        return String.format("%d", key);
    }
}
