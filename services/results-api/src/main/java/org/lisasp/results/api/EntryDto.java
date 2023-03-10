package org.lisasp.results.api;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.lisasp.results.api.value.Penalty;
import org.lisasp.results.api.value.Start;
import org.lisasp.results.api.value.Swimmer;

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
