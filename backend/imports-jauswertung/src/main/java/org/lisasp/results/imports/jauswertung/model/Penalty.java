package org.lisasp.results.imports.jauswertung.model;

import lombok.Data;

@Data
public class Penalty {
    private String name;
    private PenaltyType type;
    private short points;

    public boolean isRealPenalty() {
        if (type == PenaltyType.None) {
            return false;
        }
        if (type == PenaltyType.Points) {
            return points > 0;
        }
        return true;
    }
}
