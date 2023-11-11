package org.lisasp.basics.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This is a buffering consumer. All input is accepted and stored until it is explicitly resend.
 *
 * @param <T> Type of the stored data.
 */
public class CollectingConsumer<T> implements Consumer<T> {

    private final List<T> accepted = new ArrayList<>();

    @Override
    public void accept(T entry) {
        accepted.add(entry);
    }

    /**
     * Resends the buffered data.
     *
     * @param consumer target
     */
    public void forEach(Consumer<? super T> consumer) {
        accepted.forEach(consumer);
        accepted.clear();
    }

    /**
     * Checks if no data has yet been stored since the last send operation (or the beginning).
     *
     * @return true if no data is stored.
     */
    public boolean isEmpty() {
        return accepted.isEmpty();
    }
}
