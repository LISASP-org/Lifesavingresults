package org.lisasp.competition.authorization.service.users;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lisasp.basics.spring.jpa.BaseEntity;
import org.lisasp.competition.api.authorization.UserRole;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserRoleEntity extends BaseEntity {

    private String username;
    private String role;
    private String resourceId;

    public UserRoleEntity(String id) {
        super(id);
    }

    public static UserRoleEntity from(UserRole userRole) {
        UserRoleEntity entity = new UserRoleEntity();
        entity.setUsername(userRole.username());
        entity.setRole(userRole.role());
        entity.setResourceId(userRole.resourceId());
        return entity;
    }

    @Override
    public String toString() {
        return String.format("UserRoleEntity(id=%s, version=%d, username=%s, role=%s, resourceId=%s)", getId(), getVersion(), username, role, resourceId);
    }

    public UserRole toValueObject() {
        return UserRole.builder().username(username).role(role).resourceId(resourceId).build();
    }
}
