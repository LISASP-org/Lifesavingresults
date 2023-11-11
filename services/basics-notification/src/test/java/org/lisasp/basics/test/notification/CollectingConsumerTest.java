package org.lisasp.basics.test.notification;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.notification.CollectingConsumer;
import org.mockito.Mockito;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CollectingConsumerTest {

    private CollectingConsumer<String> consumer;
    private Consumer<String> listener;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void prepare() {
        consumer = new CollectingConsumer<>();
        listener = (Consumer<String>) Mockito.mock(Consumer.class);
    }

    @AfterEach
    void cleanup() {
        consumer = null;
        listener = null;
    }

    @Test
    void accept() {
        consumer.accept("Test");
        assertFalse(consumer.isEmpty());
    }

    @Test
    void forEach() {
        consumer.accept("Test 1");
        consumer.accept("Test 2");
        consumer.accept("Test 3");

        consumer.forEach(listener);

        verify(listener, times(1)).accept("Test 1");
        verify(listener, times(1)).accept("Test 2");
        verify(listener, times(1)).accept("Test 3");
        verifyNoMoreInteractions(listener);
    }

    @Test
    void isEmpty() {
        assertTrue(consumer.isEmpty());
    }
}
