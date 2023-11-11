package org.lisasp.basics.jre.id;

/**
 * An IdGenerator interface that provides text based ids.
 * Ids have to be unique within an instance of an IdGenerator.
 * Subclasses may fulfill further constraints.
 */
public interface IdGenerator {
    /**
     * Calculates a unique id.
     *
     * @return an unique id.
     */
    String nextId();
}
