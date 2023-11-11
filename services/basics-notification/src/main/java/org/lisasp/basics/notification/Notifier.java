package org.lisasp.basics.notification;

import lombok.Synchronized;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A relay that directly resends the given data to all registered consumers.
 *
 * @param <T> type of the transmitted data.
 */
public class Notifier<T> implements Consumer<T> {

    private final List<Consumer<T>> dataListeners = new ArrayList<>();

    @Synchronized("dataListeners")
    public void accept(T event) {
        dataListeners.forEach(dl -> dl.accept(event));
    }

    /**
     * Registers the given consumer.
     *
     * @param dataListener consumer to be registered
     */
    @Synchronized("dataListeners")
    public void register(Consumer<T> dataListener) {
        dataListeners.add(dataListener);
    }
}
