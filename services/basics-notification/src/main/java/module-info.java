/**
 * This module provides base classes for simple event based approaches.
 */
module org.lisasp.basics.notification {
    exports org.lisasp.basics.notification;
    exports org.lisasp.basics.notification.primitive;

    requires org.slf4j;

    requires static lombok;
}
