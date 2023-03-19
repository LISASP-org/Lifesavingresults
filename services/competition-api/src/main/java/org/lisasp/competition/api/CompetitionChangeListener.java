package org.lisasp.competition.api;

public interface CompetitionChangeListener {
    void added(CompetitionDto competition);
    void updated(CompetitionDto competition);
    void deleted(CompetitionDto competition);
}
