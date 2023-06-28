package org.lisasp.competition.api.authorization;

import lombok.Builder;

@Builder
public record UserRole(String username, String role, String resourceId) {
}
