package org.lisasp.competition.results.service;

public interface Updater<T> {
    boolean update(T t);
}
