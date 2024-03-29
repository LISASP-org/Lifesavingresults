package org.lisasp.basics.jre.function.primitive;

/**
 * Listener implementation for byte.
 * The naming follows the Consumer-Interface.
 */
public interface ByteConsumer {
    /**
     * Accepts one byte.
     *
     * @param data input of one byte
     */
    void accept(byte data);
}
