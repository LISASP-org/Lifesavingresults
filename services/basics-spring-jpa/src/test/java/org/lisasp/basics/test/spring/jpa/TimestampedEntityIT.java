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
class TimestampedEntityIT {

    @Autowired
    private TestTimestampedEntityRepository repository;

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
}
