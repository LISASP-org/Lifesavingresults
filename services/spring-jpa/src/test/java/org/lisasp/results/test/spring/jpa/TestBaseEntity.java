package org.lisasp.results.test.spring.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lisasp.results.spring.jpa.BaseEntity;

import jakarta.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TestBaseEntity extends BaseEntity {

    public TestBaseEntity(String id) {
        super(id);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public void simulateSave() {
        beforeSave();
    }
}
