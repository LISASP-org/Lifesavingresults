module org.lisasp.basics.test.jms {
    requires org.lisasp.basics.spring.jms;

    opens org.lisasp.basics.test.spring.jms;
    opens org.lisasp.basics.test.spring.jms.data;

    requires spring.jms;
    requires spring.beans;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.boot;

    requires jakarta.messaging;

    requires org.mockito;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;

    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.engine;

    requires static lombok;
}
