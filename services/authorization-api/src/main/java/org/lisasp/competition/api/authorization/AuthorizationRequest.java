package org.lisasp.competition.api.authorization;

import lombok.Builder;

@Builder
public record AuthorizationRequest(String username, Right right, String resourceId) {
}
