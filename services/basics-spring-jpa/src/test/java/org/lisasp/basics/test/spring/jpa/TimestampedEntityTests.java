package org.lisasp.basics.test.spring.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TimestampedEntityTests {

    @Autowired
    private TestTimestampedEntityRepository repository;

    @Test
    void construct() {
        TestTimestampedEntity entity = new TestTimestampedEntity("test");

        assertTrue(entity.isNew());
        assertNotNull(entity.getId());
        assertTrue(entity.getId().length() > 10);
    }

    @Test
    void notEqualsObject() {
        TestTimestampedEntity entity = new TestTimestampedEntity("test");

        assertFalse(entity.equals(null));
        assertNotEquals(new Object(), entity);
    }

    @Test
    void notEqualsDifferentName() {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("Id", "test1");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("Id", "test2");

        assertNotEquals(entity1, entity2);
    }

    @Test
    void notEqualsDifferentId() {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("Id1", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("Id2", "test");

        assertNotEquals(entity1, entity2);
    }

    @Test
    void notEqualsDifferentTimestamp() throws InterruptedException {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("Id", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("Id", "test");

        entity1.simulateSave();
        Thread.sleep(1);
        entity2.simulateSave();

        assertNotEquals(entity1, entity2);
        assertNotEquals(entity2, entity1);
    }

    @Test
    void equalsWithSameTimestamp() throws InterruptedException {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("Id", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("Id", "test");

        entity1.simulateSave(LocalDateTime.of(2022, 3, 9, 12, 0));
        entity2.simulateSave(LocalDateTime.of(2022, 3, 9, 12, 0));

        assertEquals(entity1, entity2);
        assertEquals(entity2, entity1);
    }

    @Test
    void notEqualsNullTimestamp() {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("Id", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("Id", "test");

        entity1.simulateSave();
        entity2.simulateSave(null);

        assertNotEquals(entity1, entity2);
        assertNotEquals(entity2, entity1);
    }

    @Test
    void equalsBothNullTimestamp() {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("Id", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("Id", "test");

        assertEquals(entity1, entity2);
        assertEquals(entity2, entity1);
    }

    @Test
    void equalsSelf() {
        TestTimestampedEntity entity = new TestTimestampedEntity("test");

        assertEquals(entity, entity);
    }

    @Test
    void equalsSame() {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("Id", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("Id", "test");

        assertEquals(entity1, entity2);
    }

    @Test
    void timestamp() {
        LocalDateTime before = LocalDateTime.now(ZoneOffset.UTC);
        TestTimestampedEntity entity = new TestTimestampedEntity("test");
        assertNotNull(entity.getId());

        repository.save(entity);
        TestTimestampedEntity actual = repository.findById(entity.getId()).orElseGet(Assertions::fail);

        LocalDateTime after = LocalDateTime.now(ZoneOffset.UTC);

        assertTrue(before.isBefore(entity.getLastModification()) || before.isEqual(entity.getLastModification()));
        assertTrue(after.isAfter(entity.getLastModification()) || after.isEqual(entity.getLastModification()));

        assertTrue(before.isBefore(actual.getLastModification()) || before.isEqual(actual.getLastModification()));
        assertTrue(after.isAfter(actual.getLastModification()) || after.isEqual(actual.getLastModification()));

        assertEquals(entity.getLastModification(), actual.getLastModification());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 10, 13})
    void versionValue(int amount) {
        TestTimestampedEntity entity = new TestTimestampedEntity();

        for (int x = 1; x <= amount; x++) {
            entity.setName("test" + x);
            repository.saveAndFlush(entity);
        }

        List<TestTimestampedEntity> actualValues = repository.findAllByName("test" + amount);
        TestTimestampedEntity actual = actualValues.get(0);

        assertEquals(1, actualValues.size());
        assertEquals(entity, actual);

        assertEquals(amount, actual.getVersion());
    }

    @Test
    void versionValue2() {
        TestTimestampedEntity entity = new TestTimestampedEntity("test1");

        repository.save(entity);
        entity.setName("test2");

        List<TestTimestampedEntity> actual = repository.findAllByName("test2");

        assertEquals(2, entity.getVersion());
        assertEquals(1, actual.size());
    }

    @Test
    void checkIntegrity() {
        TestTimestampedEntity entity = new TestTimestampedEntity("test1");

        repository.save(entity);
        entity.setName("test2");
        repository.save(entity);

        List<TestTimestampedEntity> actual = repository.findAllByName("test2");

        long size = repository.count();
        assertEquals(1, size);

        assertEquals(1, actual.size());
        assertEquals(entity.getName(), actual.get(0).getName());
        assertEquals(entity.getId(), actual.get(0).getId());
        assertEquals(entity.getVersion(), actual.get(0).getVersion());
        assertTrue(entity.getLastModification().isBefore(actual.get(0).getLastModification()) || entity.getLastModification().isEqual(actual.get(0).getLastModification()));
    }

    @Test
    void lastModificationNull() {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("id", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("id", "test");

        entity2.simulateSave();

        assertNotEquals(entity1, entity2);
        assertNotEquals(entity2, entity1);
    }

    @Test
    void lastModificationDifferent() throws InterruptedException {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("id", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("id", "test");

        entity1.simulateSave();
        Thread.sleep(1);
        entity2.simulateSave();

        assertNotEquals(entity1, entity2);
        assertNotEquals(entity2, entity1);
    }

    @Test
    void hashCodeForEqualObject() {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("id", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("id", "test");

        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void hashCodeWithDifferentTimestamp() throws InterruptedException {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("id", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("id", "test");

        entity1.simulateSave();
        Thread.sleep(1);
        entity2.simulateSave();

        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void hashCodeWithDifferentTimestampWithNull() {
        TestTimestampedEntity entity1 = new TestTimestampedEntity("id", "test");
        TestTimestampedEntity entity2 = new TestTimestampedEntity("id", "test");

        entity2.simulateSave();

        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }
}
