package org.lisasp.basics.test.spring.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BaseEntityTests {

    @Test
    void construct() {
        TestBaseEntity entity = new TestBaseEntity();

        assertTrue(entity.isNew());
        assertNotNull(entity.getId());
        assertEquals(36, entity.getId().length());
    }

    @Test
    void notEqualsObject() {
        TestBaseEntity entity = new TestBaseEntity();

        assertFalse(entity.equals(new Object()));
    }

    @Test
    void notEquals() {
        TestBaseEntity entity1 = new TestBaseEntity();
        TestBaseEntity entity2 = new TestBaseEntity();

        assertNotEquals(entity1, entity2);
    }

    @Test
    void notEqualsId() {
        TestBaseEntity entity1 = new TestBaseEntity(null);
        TestBaseEntity entity2 = new TestBaseEntity();

        assertNotEquals(entity1, entity2);
    }

    @Test
    void nullId() {
        TestBaseEntity entity1 = new TestBaseEntity();
        TestBaseEntity entity2 = new TestBaseEntity(null);

        assertNotEquals(entity1, entity2);
        assertNotEquals(entity2, entity1);
    }

    @Test
    void bothWithNullId() {
        TestBaseEntity entity1 = new TestBaseEntity(null);
        TestBaseEntity entity2 = new TestBaseEntity(null);

        assertEquals(entity1, entity2);
        assertEquals(entity2, entity1);
    }

    @Test
    void equalsSelf() {
        TestBaseEntity entity = new TestBaseEntity();

        assertEquals(entity, entity);
    }

    @Test
    void equalsSameId() {
        TestBaseEntity entity1 = new TestBaseEntity("Id");
        TestBaseEntity entity2 = new TestBaseEntity("Id");

        assertEquals(entity1, entity2);
    }

    @Test
    void differentVersion() {
        TestBaseEntity entity1 = new TestBaseEntity("Id");
        TestBaseEntity entity2 = new TestBaseEntity("Id");

        entity2.simulateSave();

        assertEquals(0, entity1.getVersion());
        assertEquals(1, entity2.getVersion());

        assertNotEquals(entity1, entity2);
    }

    @Test
    void hashCodeForEqualObject() {
        TestBaseEntity entity1 = new TestBaseEntity("Id");
        TestBaseEntity entity2 = new TestBaseEntity("Id");

        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void hashCodeWithDifferentId() {
        TestBaseEntity entity1 = new TestBaseEntity("Id1");
        TestBaseEntity entity2 = new TestBaseEntity("Id2");

        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void hashCodeWithDifferentVersion() {
        TestBaseEntity entity1 = new TestBaseEntity("Id");
        TestBaseEntity entity2 = new TestBaseEntity("Id");

        entity2.simulateSave();

        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void toStringFormat() {
        TestBaseEntity entity = new TestBaseEntity("1");
        assertEquals("BaseEntity(id=1, version=0)", entity.toString());
    }
}
