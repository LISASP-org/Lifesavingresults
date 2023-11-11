package org.lisasp.basics.jre.date;

import java.time.LocalDate;

/**
 * This is an abstraction interface for date related operations, that are linked to the current date.
 * It provides a subset of the static functionality from LocalDate as instance methods.
 */
public interface DateFacade {

    /**
     * Obtains the current date.
     *
     * @return the current date
     */
    LocalDate today();
}
