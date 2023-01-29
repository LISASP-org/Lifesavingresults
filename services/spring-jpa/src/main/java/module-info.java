module org.lisasp.results.spring.jpa {
    exports org.lisasp.results.spring.jpa;
    opens org.lisasp.results.spring.jpa;

    requires jakarta.persistence;

    requires spring.data.commons;

    requires static lombok;
}
