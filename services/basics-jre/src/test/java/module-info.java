module org.lisasp.basics.test.jre {
    requires org.lisasp.basics.jre;

    opens org.lisasp.basics.test.jre.date;
    opens org.lisasp.basics.test.jre.function;
    opens org.lisasp.basics.test.jre.function.primitive;
    opens org.lisasp.basics.test.jre.id;
    opens org.lisasp.basics.test.jre.io;

    requires org.mockito;

    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;
}
