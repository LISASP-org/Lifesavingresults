package org.lisasp.results.test.model;

import org.lisasp.basics.spring.jpa.BaseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

class TestDoubleCrudRepository<T extends BaseEntity> implements CrudRepository<T, String> {

    protected final Map<String, T> entries = new HashMap<>();

    @Override
    public <S extends T> S save(S entity) {
        return (S) entries.put(entity.getId(), entity);
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(e -> save(e)).toList();
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(entries.getOrDefault(id, null));
    }

    @Override
    public boolean existsById(String id) {
        return entries.containsKey(id);
    }

    @Override
    public Iterable<T> findAll() {
        return entries.values().stream().toList();
    }

    @Override
    public Iterable<T> findAllById(Iterable<String> ids) {
        return StreamSupport.stream(ids.spliterator(), false).map(id -> findById(id)).filter(opt -> opt.isPresent()).map(opt -> opt.get()).toList();
    }

    @Override
    public long count() {
        return entries.size();
    }

    @Override
    public void deleteById(String id) {
        entries.remove(id);
    }

    @Override
    public void delete(T entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        StreamSupport.stream(ids.spliterator(), false).forEach(id -> deleteById(id));
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        StreamSupport.stream(entities.spliterator(), false).forEach(e -> delete(e));
    }

    @Override
    public void deleteAll() {
        entries.clear();
    }
}
