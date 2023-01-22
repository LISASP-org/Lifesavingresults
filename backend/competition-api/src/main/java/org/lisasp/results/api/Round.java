package org.lisasp.results.api;

import lombok.Value;

@Value
public class Round {
    private byte round;
    private boolean isFinal;

    private boolean isFinal() {
        return isFinal;
    }
}
