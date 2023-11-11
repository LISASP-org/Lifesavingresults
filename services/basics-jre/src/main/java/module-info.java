/**
 * This module provides abstractions to (mostly static) functionality provided by the jre.
 * It is focussed on providing better modularity and testability.
 */
module org.lisasp.basics.jre {
    exports org.lisasp.basics.jre.io;
    exports org.lisasp.basics.jre.date;
    exports org.lisasp.basics.jre.id;

    requires static lombok;
}
