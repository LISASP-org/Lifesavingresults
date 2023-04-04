package org.lisasp.competition.results.api.imports;

import lombok.Builder;
import org.lisasp.competition.results.api.value.Penalty;
import org.lisasp.competition.results.api.value.SplitTime;
import org.lisasp.competition.results.api.value.Start;
import org.lisasp.competition.results.api.value.Swimmer;

@Builder
public record Entry(String number, String name, String club, String nationality, int timeInMillis, byte placeInHeat, Penalty[] penalties, Swimmer[] swimmer,
                    SplitTime[] splitTimes, Start start) {}
