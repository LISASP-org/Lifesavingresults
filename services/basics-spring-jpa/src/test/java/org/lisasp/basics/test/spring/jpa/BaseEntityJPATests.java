package org.lisasp.basics.test.spring.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BaseEntityJPATests {

    @Autowired
    private TestJPABaseEntityRepository repository;

    @Test
    void construct() {
        TestJPABaseEntity entity = new TestJPABaseEntity("test");

        assertTrue(entity.isNew());
        assertNotNull(entity.getId());
        assertTrue(entity.getId().length() > 30);
    }

    @Test
    void notEqualsObject() {
        TestJPABaseEntity entity = new TestJPABaseEntity("test");

        assertNotEquals(new Object(), entity);
    }

    @Test
    void notEqualsDifferentName() {
        TestJPABaseEntity entity1 = new TestJPABaseEntity("Id", "test1");
        TestJPABaseEntity entity2 = new TestJPABaseEntity("Id", "test2");

        assertNotEquals(entity1, entity2);
    }

    @Test
    void equalsSelf() {
        TestJPABaseEntity entity = new TestJPABaseEntity("test");

        assertEquals(entity, entity);
    }

    @Test
    void equalsSame() {
        TestJPABaseEntity entity1 = new TestJPABaseEntity("Id", "test");
        TestJPABaseEntity entity2 = new TestJPABaseEntity("Id", "test");

        assertEquals(entity1, entity2);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 10, 13})
    void versionValue(int amount) {
        TestJPABaseEntity entity = new TestJPABaseEntity();

        for (int x = 1; x <= amount; x++) {
            entity.setName("test" + x);
            repository.saveAndFlush(entity);
        }

        List<TestJPABaseEntity> actualValues = repository.findAllByName("test" + amount);
        TestJPABaseEntity actual = actualValues.get(0);

        assertEquals(1, actualValues.size());
        assertEquals(entity, actual);

        assertEquals(amount, actual.getVersion());
    }

    @Test
    void versionValue2() {
        TestJPABaseEntity entity = new TestJPABaseEntity("test1");

        repository.save(entity);
        entity.setName("test2");

        List<TestJPABaseEntity> actual = repository.findAllByName("test2");

        assertEquals(2, entity.getVersion());
    }

    @Test
    void checkIntegrity() {
        TestJPABaseEntity entity = new TestJPABaseEntity("test1");

        repository.save(entity);
        entity.setName("test2");
        repository.save(entity);

        List<TestJPABaseEntity> actual = repository.findAllByName("test2");

        long size = repository.count();
        assertEquals(1, size);

        assertEquals(1, actual.size());
        assertEquals(entity.getName(), actual.get(0).getName());
        assertEquals(entity.getId(), actual.get(0).getId());
        assertEquals(entity.getVersion(), actual.get(0).getVersion());
    }

    @Test
    void isNew() {
        TestJPABaseEntity entity = new TestJPABaseEntity("test1");
        assertTrue(entity.isNew());

        repository.save(entity);

        List<TestJPABaseEntity> actual = repository.findAllByName("test1");

        long size = repository.count();
        assertEquals(1, size);

        assertEquals(1, actual.size());
        assertEquals(entity.getName(), actual.get(0).getName());
        assertEquals(entity.getId(), actual.get(0).getId());
        assertEquals(entity.getVersion(), actual.get(0).getVersion());
    }

    @Test
    void notIsNew() {
        TestJPABaseEntity entity = new TestJPABaseEntity("test1");

        repository.save(entity);
        assertFalse(entity.isNew());

        List<TestJPABaseEntity> actual = repository.findAllByName("test1");
        assertFalse(entity.isNew());
    }
}
