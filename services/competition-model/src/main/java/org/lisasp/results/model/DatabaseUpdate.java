package org.lisasp.results.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DatabaseUpdate<T> {

    private final List<T> toDelete = new ArrayList<>();
    private final List<T> toSave = new ArrayList<>();

    private final CrudRepository<T, String> repository;
    private final DatabaseUpdate<?> inner;

    public void delete(T t) {
        toDelete.add(t);
    }

    public void save(T t) {
        toSave.add(t);
    }

    public void invokeUpdate() {
        repository.saveAll(toSave);
        toSave.clear();

        if (inner != null) {
            inner.invokeUpdate();
        }
        
        repository.deleteAll(toDelete);
        toDelete.clear();
    }
}
