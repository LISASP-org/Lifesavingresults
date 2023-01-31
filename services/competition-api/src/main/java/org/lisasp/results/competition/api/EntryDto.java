package org.lisasp.results.competition.api;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.lisasp.results.base.api.type.EventType;
import org.lisasp.results.base.api.type.Gender;
import org.lisasp.results.base.api.value.Penalty;
import org.lisasp.results.base.api.value.Start;
import org.lisasp.results.base.api.value.Swimmer;

@Value
@AllArgsConstructor
public class EntryDto {
    private final String id;
    private final long version;
    private String number;
    private String name;
    private String club;
    private String nationality;
    private int timeInMillis;
    private int placeInHeat;
    private Penalty[] penalties;
    private Swimmer[] swimmer;
    private Start start;
}
