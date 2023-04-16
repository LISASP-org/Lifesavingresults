package org.lisasp.competition.authorization.service.users;

import lombok.Builder;

@Builder
public record UserRole(String username, String role, String resourceId) {
}
