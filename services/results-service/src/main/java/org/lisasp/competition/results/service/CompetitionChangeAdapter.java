package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.CompetitionChangeListener;
import org.lisasp.competition.api.CompetitionDto;

@RequiredArgsConstructor
public class CompetitionChangeAdapter implements CompetitionChangeListener {

    private final ResultService service;

    @Override
    public void added(CompetitionDto competition) {
        service.addOrUpdate(competition);
    }

    @Override
    public void updated(CompetitionDto competition) {
        service.addOrUpdate(competition);
    }

    @Override
    public void deleted(CompetitionDto competition) {
        service.delete(competition.id());
    }
}
