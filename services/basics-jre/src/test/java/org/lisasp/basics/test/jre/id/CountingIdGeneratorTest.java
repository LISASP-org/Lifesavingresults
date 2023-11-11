package org.lisasp.basics.test.jre.id;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.jre.id.CountingIdGenerator;
import org.lisasp.basics.jre.id.IdGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountingIdGeneratorTest {
    private IdGenerator idGenerator;

    @BeforeEach
    void beforeEachTest() {
        idGenerator = new CountingIdGenerator();
    }

    @Test
    void firstCall() {
        String id = idGenerator.nextId();

        assertEquals("1", id);
    }

    @Test
    void secondCall() {
        idGenerator.nextId();

        String id = idGenerator.nextId();

        assertEquals("2", id);
    }

    @Test
    void thirdCall() {
        idGenerator.nextId();
        idGenerator.nextId();

        String id = idGenerator.nextId();

        assertEquals("3", id);
    }
}
