package org.lisasp.results.spring.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class TimestampedEntity extends BaseEntity {

    protected TimestampedEntity(String id) {
        super(id);
    }

    @Column(nullable = false)
    private LocalDateTime lastModification;

    @Override
    protected void beforeSave() {
        super.beforeSave();
        lastModification = LocalDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TimestampedEntity entity)) {
            return false;
        }
        if ((lastModification == null) != (entity.getLastModification() == null)) {
            return false;
        }
        if (lastModification != null && !lastModification.equals(entity.getLastModification())) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lastModification);
    }
}
