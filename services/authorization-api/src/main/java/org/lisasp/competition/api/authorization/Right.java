package org.lisasp.competition.api.authorization;

import lombok.Builder;

@Builder
public record Right(String module, String name) {
}
