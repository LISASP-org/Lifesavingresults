package org.lisasp.competition.results.api;

import org.lisasp.competition.results.api.value.Penalty;
import org.lisasp.competition.results.api.value.Start;
import org.lisasp.competition.results.api.value.Swimmer;

public record EntryDto(String id, long version, String number, String name, String club, String nationality, int timeInMillis, int placeInHeat,
                       Penalty[] penalties, Swimmer[] swimmer, Start start) {
}
