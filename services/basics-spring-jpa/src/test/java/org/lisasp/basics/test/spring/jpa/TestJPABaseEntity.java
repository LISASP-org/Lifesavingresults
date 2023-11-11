package org.lisasp.basics.test.spring.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lisasp.basics.spring.jpa.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestJPABaseEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    public TestJPABaseEntity(String id, String name) {
        super(id);
        setName(name);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        TestJPABaseEntity entity = (TestJPABaseEntity) o;
        if ((name == null) != (entity.getName() == null)) {
            return false;
        }
        return name == null || name.equals(entity.getName());
    }

    public String toString() {
        return String.format("TestEntity(%s, %s)", getId(), getName());
    }
}
