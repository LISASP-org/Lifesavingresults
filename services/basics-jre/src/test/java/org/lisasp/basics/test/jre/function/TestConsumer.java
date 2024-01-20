package org.lisasp.basics.test.jre.function;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TestConsumer<T> implements Consumer<T> {

    private Set<T> testValues = new HashSet<>();

    void setTestValues(T... testValues) {
        this.testValues = new HashSet<>(Set.of(testValues));
    }

    @Override
    public void accept(T s) {
        if (!testValues.remove(s)) {
            fail("Invalid value: " + s);
        }
    }

    void assertAllValuesWereConsumed() {
        assertEquals(0, testValues.size(), "Not all values were consumed.");
    }
}
