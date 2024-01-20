package org.lisasp.basics.test.jre.function;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.jre.function.Notifier;
import org.mockito.Mockito;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class NotifierTest {

    private Notifier<String> notifier;
    private TestConsumer<String> listener;

    @BeforeEach
    void prepare() {
        listener = new TestConsumer<>();

        notifier = new Notifier<>();
        notifier.register(listener);
    }

    @Test
    void acceptEmpty() {
        listener.assertAllValuesWereConsumed();
    }

    @Test
    void acceptOne() {
        listener.setTestValues("Test");

        notifier.accept("Test");

        listener.assertAllValuesWereConsumed();
    }

    @Test
    void acceptTwo() {
        listener.setTestValues("Test 1", "Test 2");

        notifier.accept("Test 1");
        notifier.accept("Test 2");

        listener.assertAllValuesWereConsumed();
    }
}
