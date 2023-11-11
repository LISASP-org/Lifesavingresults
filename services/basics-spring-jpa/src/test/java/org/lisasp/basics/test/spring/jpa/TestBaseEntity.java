package org.lisasp.basics.test.spring.jpa;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lisasp.basics.spring.jpa.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TestBaseEntity extends BaseEntity {

    public TestBaseEntity(String id) {
        super(id);
    }

    public void simulateSave() {
        beforeSave();
    }
}
