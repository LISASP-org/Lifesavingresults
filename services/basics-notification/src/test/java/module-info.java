module org.lisasp.basics.test.notifier {
    requires org.lisasp.basics.notification;

    opens org.lisasp.basics.test.notification;
    opens org.lisasp.basics.test.notification.primitive;

    requires org.mockito;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;

    requires transitive org.junit.jupiter.engine;
    requires transitive org.junit.jupiter.api;
}
