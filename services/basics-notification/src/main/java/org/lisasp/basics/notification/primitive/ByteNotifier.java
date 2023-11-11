package org.lisasp.basics.notification.primitive;

import lombok.Synchronized;

import java.util.ArrayList;
import java.util.List;

/**
 * Notifies all registered listeners, when a new byte is accepted.
 */
public class ByteNotifier implements ByteConsumer {

    private final List<ByteConsumer> dataListeners = new ArrayList<>();

    @Override
    @Synchronized("dataListeners")
    public void accept(byte event) {
        dataListeners.forEach(dl -> dl.accept(event));
    }

    /**
     * Registers a given ByteConsumer.
     *
     * @param dataListener ByteConsumer to be registered
     */
    @Synchronized("dataListeners")
    public void register(ByteConsumer dataListener) {
        dataListeners.add(dataListener);
    }
}
