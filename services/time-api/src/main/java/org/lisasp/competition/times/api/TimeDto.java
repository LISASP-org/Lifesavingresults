package org.lisasp.competition.times.api;

import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;

public record TimeDto(String id, String competition, String acronym, EventType eventType, String name, String club, String nationality,
                      String agegroup, Gender gender, String discipline, int timeInMillis) {}
