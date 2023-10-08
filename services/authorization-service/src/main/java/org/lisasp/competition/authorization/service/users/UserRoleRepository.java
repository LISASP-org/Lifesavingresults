package org.lisasp.competition.authorization.service.users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends CrudRepository<UserRoleEntity, String> {
    boolean existsByUsernameAndRoleAndResourceId(String username, String role, String resourceId);

    void deleteByUsernameAndRoleAndResourceId(String username, String role, String resourceId);

    @Query("SELECT u.role FROM UserRoleEntity u WHERE u.username = :username AND u.resourceId = :resourceId")
    String[] findAllRolesByUsernameAndResourceId(@Param("username") String username, @Param("resourceId") String resourceId);

    List<UserRoleEntity> findAllByUsername(String username);
}
