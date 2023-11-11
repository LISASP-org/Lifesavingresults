package org.lisasp.basics.spring.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * BaseEntity extension with modification timestamp handling
 */
@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class TimestampedEntity extends BaseEntity {

    /**
     * Constructor
     *
     * @param id id of the entity
     */
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
