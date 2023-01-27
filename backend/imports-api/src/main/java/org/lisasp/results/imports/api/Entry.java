package org.lisasp.results.imports.api;

import lombok.Builder;
import lombok.Value;
import org.lisasp.results.base.api.value.Penalty;
import org.lisasp.results.base.api.value.Start;
import org.lisasp.results.base.api.value.Swimmer;

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
