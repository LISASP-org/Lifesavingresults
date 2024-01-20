package org.lisasp.basics.test.jre.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.jre.function.ExceptionCatchingNotifier;
import org.lisasp.basics.jre.function.Notifier;

class ExceptionCatchingNotifierTest {

    private Notifier<String> notifier;
    private TestConsumer<String> listener;

    @BeforeEach
    void prepare() {
        listener = new TestConsumer<>();

        notifier = new ExceptionCatchingNotifier<>();
        notifier.register(listener);
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

    @Test
    void catchException() {
        listener.setTestValues("Test");
        notifier.register(s -> {
            throw new RuntimeException();
        });

        notifier.accept("Test");

        listener.assertAllValuesWereConsumed();
    }
}
