package org.lisasp.competition.results.api;

import lombok.Builder;
import org.lisasp.competition.base.api.EventType;

@Builder
public record TimeChangedEvent(EventType type, CompetitionDto competition, EventDto event, EntryDto entry) {}
