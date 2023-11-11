package org.lisasp.basics.jre.date;

import java.time.LocalDate;

/**
 * ActualDate uses the LocalDate class to gather date related information.
 */
public class ActualDate implements DateFacade {

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }
}
