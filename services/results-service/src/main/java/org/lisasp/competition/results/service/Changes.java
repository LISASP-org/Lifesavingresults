package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
class Changes<T> {

    private final List<T> toDelete = new ArrayList<>();
    private final List<T> toSave = new ArrayList<>();

    private final CrudRepository<T, String> repository;
    private final Changes<?> inner;
    private final ChangeListener listener;

    public void delete(T t) {
        toDelete.add(t);
    }

    public void save(T t) {
        toSave.add(t);
    }

    public void invokeUpdate() {
        repository.saveAll(toSave);
        if (listener != null) {
            listener.changed(Collections.unmodifiableList(toSave));
        }
        toSave.clear();

        if (null != inner) {
            inner.invokeUpdate();
        }

        if (listener != null) {
            listener.deleted(Collections.unmodifiableList(toDelete));
        }
        repository.deleteAll(toDelete);
        toDelete.clear();
    }
}
