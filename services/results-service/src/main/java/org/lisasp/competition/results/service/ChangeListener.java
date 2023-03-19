package org.lisasp.competition.results.service;

import java.util.Collection;
import java.util.List;

interface ChangeListener<T> {
    void changed(Collection<T> t);

    void deleted(Collection<T> t);
}
