package org.lisasp.competition.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.api.authorization.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompetitionServerAuthorizationService {

    // private final AuthorizationService authorizationService;

    public void firstUserRole(Roles role, String resourceId) {
        //authorizationService.addUserRole(getUsername(), role.getName(), resourceId);
    }

    public void addUserRole(Roles role, String resourceId) {
        assertAuthorization(Rights.AuthorizationCompetition, resourceId);
        addUserRole(getUsername(), role, resourceId);
    }

    public void addUserRole(String username, Roles role, String resourceId) {
        assertAuthorization(Rights.AuthorizationCompetition, resourceId);
        //authorizationService.addUserRole(username, role.getName(), resourceId);
    }

    public void removeUserRole(String username, Roles role, String resourceId) {
        assertAuthorization(Rights.AuthorizationCompetition, resourceId);
        //authorizationService.removeUserRole(username, role.getName(), resourceId);
    }

    public void assertAuthorization(Rights right, String resourceId) {
        //if (!authorizationService.hasAuthorization(new AuthorizationRequest(getUsername(), right.toAction(), resourceId))) {
            log.info(String.format("User '%s' is not authorized for right '%s' on resourceId with id '%s'.", getUsername(), right, resourceId));
            throw new AccessDeniedException(String.format("User '%s' is not authorized for right '%s' on resourceId with id '%s'.", getUsername(), right, resourceId));
        //}
    }

    public UserRole[] findRoles() {
        return findRoles(getUsername());
    }

    public UserRole[] findRoles(String username) {
        assertSelf(username);
        //return authorizationService.findRoles(username);
        return new UserRole[0];
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void assertSelf(String username) {
        if (!username.equalsIgnoreCase(getUsername())) {
            throw new AccessDeniedException(String.format("User '%s' is not authorized to access info of user '%s'.", getUsername(), username));
        }
    }

}
