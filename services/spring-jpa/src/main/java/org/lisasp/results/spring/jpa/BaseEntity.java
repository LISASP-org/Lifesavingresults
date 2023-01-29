package org.lisasp.results.spring.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.domain.Persistable;

import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseEntity implements Persistable<String> {

    protected BaseEntity() {
        id = UUID.randomUUID().toString();
    }

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
