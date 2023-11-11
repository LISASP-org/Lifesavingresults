module org.lisasp.basics.test.jpa {
    requires org.lisasp.basics.spring.jpa;

    opens org.lisasp.basics.test.spring.jpa to spring.core, org.hibernate.orm.core, org.junit.platform.commons;

    requires jakarta.persistence;

    requires spring.data.jpa;
    requires spring.beans;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.tx;

    requires spring.test;
    requires spring.boot.test.autoconfigure;

    requires org.mockito;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;

    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.engine;
    requires transitive org.junit.jupiter.params;

    requires static lombok;
}
