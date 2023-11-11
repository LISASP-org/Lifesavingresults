package org.lisasp.basics.test.notification;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.notification.ExceptionCatchingNotifier;
import org.lisasp.basics.notification.Notifier;
import org.mockito.Mockito;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class ExceptionCatchingNotifierTest {

    private Notifier<String> notifier;
    private Consumer<String> listener;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void prepare() {
        listener = Mockito.mock(Consumer.class);

        notifier = new ExceptionCatchingNotifier<>();
        notifier.register(listener);
    }

    @AfterEach
    void cleanup() {
        notifier = null;
        listener = null;
    }

    @Test
    void acceptEmpty() {
        verifyNoMoreInteractions(listener);
    }

    @Test
    void acceptOne() {
        notifier.accept("Test");

        verify(listener, times(1)).accept("Test");
        verifyNoMoreInteractions(listener);
    }

    @Test
    void acceptTwo() {
        notifier.accept("Test 1");
        notifier.accept("Test 2");

        verify(listener, times(1)).accept("Test 1");
        verify(listener, times(1)).accept("Test 2");
        verifyNoMoreInteractions(listener);
    }

    @Test
    void catchException() {
        notifier.register(s -> {
            throw new RuntimeException();
        });

        notifier.accept("Test");

        verify(listener, times(1)).accept("Test");
        verifyNoMoreInteractions(listener);
    }
}
