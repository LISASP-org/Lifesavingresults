package org.lisasp.basics.test.jre.date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.jre.date.ActualDateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActualDateTimeTest {

    private ActualDateTime actualDateTime;

    @BeforeEach
    void prepare() {
        actualDateTime = new ActualDateTime();
    }

    @AfterEach
    void cleanup() {
        actualDateTime = null;
    }

    @Test
    void today() {
        // Consider before and after call dates to tackle date switch problems during test.
        LocalDateTime before = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime actual = actualDateTime.now();
        LocalDateTime after = LocalDateTime.now(ZoneId.of("UTC"));

        assertNotNull(actual);
        assertTrue(before.compareTo(actual) <= 0);
        assertTrue(after.compareTo(actual) >= 0);
    }
}
