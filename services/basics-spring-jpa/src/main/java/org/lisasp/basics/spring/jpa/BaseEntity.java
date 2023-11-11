package org.lisasp.basics.spring.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.util.Objects;
import java.util.UUID;

/**
 * Entity implementation with id and version handling.
 */
@MappedSuperclass
@Getter
@ToString
public abstract class BaseEntity implements Persistable<String> {

    /**
     * Constructor
     */
    protected BaseEntity() {
        id = UUID.randomUUID().toString();
    }

    /**
     * Constructor
     *
     * @param id id of the entity
     */
    protected BaseEntity(String id) {
        this.id = id;
    }

    @Id
    @Column(nullable = false, length = 40)
    private String id;

    @Version
    @Column(nullable = false)
    private int version = 0;

    @Override
    public boolean isNew() {
        return version < 1;
    }

    /**
     * JPA internal method to provide version increment.
     */
    @PrePersist
    @PreUpdate
    protected void beforeSave() {
        version++;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseEntity entity)) {
            return false;
        }
        if ((id == null) != (entity.getId() == null)) {
            return false;
        }
        if (id != null && !id.equals(entity.getId())) {
            return false;
        }
        return version == entity.getVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }
}
