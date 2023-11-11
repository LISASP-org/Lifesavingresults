package org.lisasp.basics.test.spring.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestJPABaseEntityRepository extends JpaRepository<TestJPABaseEntity, String> {
    List<TestJPABaseEntity> findAllByName(String name);
}
