package org.lisasp.competition.authorization.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.authorization.AuthorizationRequest;
import org.lisasp.competition.api.authorization.UserRole;
import org.lisasp.competition.authorization.service.users.UserRoleService;

import java.util.Arrays;

@RequiredArgsConstructor
public class AuthorizationService {

    private final RoleService roleService;
    private final UserRoleService userRoleService;

    public boolean hasAuthorization(AuthorizationRequest request) {
        String[] roles = userRoleService.findRoles(request.username(), request.resourceId());
        return roleService.hasAuthorization(Arrays.stream(roles).toArray(String[]::new), request.right());
        // return true;
    }

    public void addUserRole(String username, String role, String resourceId) {
        userRoleService.add(new UserRole(username, role, resourceId));
    }

    public void removeUserRole(String username, String role, String resourceId) {
        userRoleService.remove(new UserRole(username, role, resourceId));
    }

    public UserRole[] findRoles(String username) {
        return userRoleService.findRoles(username);
    }
}
