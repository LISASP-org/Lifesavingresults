package org.lisasp.basics.jre.date;

import java.time.LocalDateTime;

/**
 * This is an abstraction interface for time related operations, that are linked to the current time.
 * It provides a subset of the static functionality from LocalDateTime as instance methods.
 */
public interface DateTimeFacade {

    /**
     * Obtains the current time.
     *
     * @return the current time
     */
    LocalDateTime now();
}
