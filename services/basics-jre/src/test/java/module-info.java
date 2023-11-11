module org.lisasp.basics.test.jre {
    requires org.lisasp.basics.jre;

    opens org.lisasp.basics.test.jre.date;
    opens org.lisasp.basics.test.jre.io;
    opens org.lisasp.basics.test.jre.id;

    requires transitive org.junit.jupiter.engine;
    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;
}
