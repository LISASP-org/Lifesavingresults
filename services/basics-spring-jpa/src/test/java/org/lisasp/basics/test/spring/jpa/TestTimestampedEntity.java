package org.lisasp.basics.test.spring.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import org.lisasp.basics.spring.jpa.TimestampedEntity;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestTimestampedEntity extends TimestampedEntity {

    @Column(nullable = false)
    private String name;

    public TestTimestampedEntity(String id, String name) {
        super(id);
        setName(name);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        TestTimestampedEntity entity = (TestTimestampedEntity) o;
        if ((name == null) != (entity.getName() == null)) {
            return false;
        }
        return name == null || name.equals(entity.getName());
    }

    public void simulateSave() {
        beforeSave();
    }

    public String toString() {
        return String.format("TestEntity(%s, %d, %s, %s)",
                getId(),
                getVersion(),
                getLastModification() == null ? "<null>" : getLastModification().toString(),
                getName());
    }

    @SneakyThrows
    public void simulateSave(LocalDateTime timestamp) {
        beforeSave();
        Field field = TimestampedEntity.class.getDeclaredField("lastModification");
        field.setAccessible(true);
        field.set(this, timestamp);
        field.setAccessible(false);
    }
}
