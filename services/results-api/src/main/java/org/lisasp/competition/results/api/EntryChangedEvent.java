package org.lisasp.competition.results.api;

import lombok.Builder;
import lombok.ToString;
import org.lisasp.competition.base.api.ChangeType;

@Builder
public record EntryChangedEvent(ChangeType type, CompetitionDto competition, EventDto event, EntryDto entry) {}
