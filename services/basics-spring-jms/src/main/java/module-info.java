/**
 * This module provides extensions to spring.jms.
 */
module org.lisasp.basics.spring.jms {
    exports org.lisasp.basics.spring.jms;

    requires org.slf4j;
    requires spring.jms;
    requires com.fasterxml.jackson.databind;
    requires jakarta.messaging;

    requires static lombok;
    requires org.jetbrains.annotations;
}
