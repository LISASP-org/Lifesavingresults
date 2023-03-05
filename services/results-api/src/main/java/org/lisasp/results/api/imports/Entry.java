package org.lisasp.results.api.imports;

import lombok.Builder;
import lombok.Value;
import org.lisasp.results.api.value.Penalty;
import org.lisasp.results.api.value.Start;
import org.lisasp.results.api.value.Swimmer;

@Value
@Builder
public class Entry {
    private String number;
    private String name;
    private String club;
    private String nationality;
    private int timeInMillis;
    private byte placeInHeat;
    private Penalty[] penalties;
    private Swimmer[] swimmer;
    private Start start;
}
