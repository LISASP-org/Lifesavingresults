package org.lisasp.basics.test.spring.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestTimestampedEntityRepository extends JpaRepository<TestTimestampedEntity, String> {
    List<TestTimestampedEntity> findAllByName(String name);
}
