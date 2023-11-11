package org.lisasp.basics.notification;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * Convenience class that logs exceptions but ensures undisturbed continuation.
 *
 * @param <T> Type of the transmitted data
 */
@Slf4j
public class ExceptionCatchingNotifier<T> extends Notifier<T> {

    @Override
    public void register(Consumer<T> dataListener) {
        super.register(new ExceptionCatchingConsumer(dataListener));
    }

    private class ExceptionCatchingConsumer implements Consumer<T> {
        private final Consumer<T> dataListener;

        public ExceptionCatchingConsumer(Consumer<T> dataListener) {
            this.dataListener = dataListener;
        }

        @Override
        public void accept(T event) {
            try {
                dataListener.accept(event);
            } catch (RuntimeException re) {
                log.warn("Problem notifying listener.", re);
            }
        }
    }
}
