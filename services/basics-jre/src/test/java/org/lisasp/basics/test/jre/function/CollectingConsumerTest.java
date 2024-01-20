package org.lisasp.basics.test.jre.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.jre.function.CollectingConsumer;

import static org.junit.jupiter.api.Assertions.*;

class CollectingConsumerTest {

    private CollectingConsumer<String> consumer;
    private TestConsumer<String> listener;

    @BeforeEach
    void prepare() {
        consumer = new CollectingConsumer<>();
        listener = new TestConsumer<>();
    }

    @Test
    void accept() {
        consumer.accept("Test");

        assertFalse(consumer.isEmpty());
        listener.assertAllValuesWereConsumed();
    }

    @Test
    void forEach() {
        listener.setTestValues("Test 1", "Test 2", "Test 3");

        consumer.accept("Test 1");
        consumer.accept("Test 2");
        consumer.accept("Test 3");

        consumer.forEach(listener);

        listener.assertAllValuesWereConsumed();
    }

    @Test
    void isEmpty() {
        assertTrue(consumer.isEmpty());
    }

}
