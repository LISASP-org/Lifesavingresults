package org.lisasp.competition.authorization.service.users;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.authorization.UserRole;

@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository repository;

    public void add(UserRole userRole) {
        if (!repository.existsByUsernameAndRoleAndResourceId(userRole.username(), userRole.role(), userRole.resourceId())) {
            repository.save(UserRoleEntity.from(userRole));
        }
    }

    public void remove(UserRole userRole) {
        repository.deleteByUsernameAndRoleAndResourceId(userRole.username(), userRole.role(), userRole.resourceId());
    }

    public String[] findRoles(String username, String resourceId) {
        return repository.findAllRolesByUsernameAndResourceId(username, resourceId);
    }

    public UserRole[] findRoles(String username) {
        return repository.findAllByUsername(username).stream().map(ur -> ur.toValueObject()).toArray(UserRole[]::new);
    }
}