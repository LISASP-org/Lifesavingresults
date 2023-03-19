package org.lisasp.competition.results.api;

import lombok.Builder;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.results.api.value.Round;

@Builder
public record EventDto(String id, long version, String agegroup, EventType eventType, Gender gender, String discipline, Round round,
                       InputValueType inputValueType) {
}
