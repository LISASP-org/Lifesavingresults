package org.lisasp.basics.test.jre.date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.jre.date.ActualDate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActualDateTest {

    private ActualDate actualDate;

    @BeforeEach
    void prepare() {
        actualDate = new ActualDate();
    }

    @AfterEach
    void cleanup() {
        actualDate = null;
    }

    @Test
    void today() {
        // Consider before and after call dates to tackle date switch problems during test.
        LocalDate before = LocalDate.now();
        LocalDate actual = actualDate.today();
        LocalDate after = LocalDate.now();

        assertNotNull(actual);
        assertTrue(before.equals(actual) || after.equals(actual));
    }
}
