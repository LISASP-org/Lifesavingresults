/**
 * This module provides abstractions to (mostly static) functionality provided by the jre.
 * It is focussed on providing better modularity and testability.
 */
module org.lisasp.basics.jre {
    exports org.lisasp.basics.jre.date;
    exports org.lisasp.basics.jre.function;
    exports org.lisasp.basics.jre.function.primitive;
    exports org.lisasp.basics.jre.id;
    exports org.lisasp.basics.jre.io;

    requires org.slf4j;

    requires static lombok;
}
