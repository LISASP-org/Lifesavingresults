package org.lisasp.competition.times.api;

import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;

public record TimeDto(String id, long version, EventType eventType, String name, String club, String nationality, String agegroup, Gender gender,
                      String discipline, int value, String penalties) {
}
