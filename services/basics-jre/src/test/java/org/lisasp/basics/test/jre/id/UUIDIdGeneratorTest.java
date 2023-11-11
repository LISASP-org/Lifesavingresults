package org.lisasp.basics.test.jre.id;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.jre.id.IdGenerator;
import org.lisasp.basics.jre.id.UUIDGenerator;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UUIDIdGeneratorTest {

    private static final Pattern UUIDPattern = Pattern.compile("^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$");

    private IdGenerator idGenerator;

    @BeforeEach
    void beforeEachTest() {
        idGenerator = new UUIDGenerator();
    }

    @Test
    void firstCall() {
        String id1 = idGenerator.nextId();

        assertTrue(UUIDPattern.matcher(id1).matches(), String.format("Generated UUID was '%s'", id1));
    }

    @Test
    void secondCall() {
        String id1 = idGenerator.nextId();
        String id2 = idGenerator.nextId();

        assertTrue(UUIDPattern.matcher(id1).matches(), String.format("Generated UUID was '%s'", id1));
        assertTrue(UUIDPattern.matcher(id2).matches(), String.format("Generated UUID was '%s'", id2));

        assertTrue(!id1.equals(id2));
    }

    @Test
    void thirdCall() {
        String id1 = idGenerator.nextId();
        String id2 = idGenerator.nextId();
        String id3 = idGenerator.nextId();

        assertTrue(UUIDPattern.matcher(id1).matches(), String.format("Generated UUID was '%s'", id1));
        assertTrue(UUIDPattern.matcher(id2).matches(), String.format("Generated UUID was '%s'", id2));
        assertTrue(UUIDPattern.matcher(id3).matches(), String.format("Generated UUID was '%s'", id3));

        assertTrue(!id1.equals(id2));
        assertTrue(!id1.equals(id3));
        assertTrue(!id2.equals(id3));
    }
}
