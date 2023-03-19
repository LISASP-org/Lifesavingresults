package org.lisasp.competition.service;

import org.lisasp.competition.api.CompetitionChangeListener;
import org.lisasp.competition.api.CompetitionDto;

import java.util.ArrayList;
import java.util.List;

class CompetitionChangeNotifier {
    private final List<CompetitionChangeListener> listeners = new ArrayList<>();

    void added(CompetitionDto competition) {
        listeners.forEach(l -> l.added(competition));
    }

    void updated(CompetitionDto competition) {
        listeners.forEach(l -> l.updated(competition));
    }

    void deleted(CompetitionDto competition) {
        listeners.forEach(l -> l.deleted(competition));
    }

    public void add(CompetitionChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
}
