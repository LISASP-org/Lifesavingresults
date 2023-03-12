package org.lisasp.results.api.value;

import lombok.Value;

@Value
public class SplitTime {
    private final byte position;
    private final int timeInMillis;
    private final byte placeInHeat;
}
