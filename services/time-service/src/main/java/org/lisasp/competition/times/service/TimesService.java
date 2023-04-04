package org.lisasp.competition.times.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.lisasp.competition.base.api.ChangeType;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.results.api.CompetitionDto;
import org.lisasp.competition.results.api.EntryChangedEvent;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.EventDto;
import org.lisasp.competition.times.api.TimeChangedEvent;
import org.lisasp.competition.times.api.TimeDto;
import org.lisasp.competition.times.api.TimesQuery;

import java.util.Arrays;
import java.util.Comparator;

@Slf4j
@RequiredArgsConstructor
public class TimesService {

    private final TimesRepository repository;

    private final AgegroupMapper agegroupMapper = new AgegroupMapper();
    private final DisciplineMapper disciplineMapper = new DisciplineMapper();

    public void receive(EntryChangedEvent entryChangedEvent) {
        TimeChangedEvent timeChangedEvent = mapToTimeChangedEvent(entryChangedEvent);
        ChangeType changeType = timeChangedEvent.changeType();

        String agegroup = agegroupMapper.execute(timeChangedEvent);
        String discipline = disciplineMapper.execute(timeChangedEvent);
        if (agegroup == null || agegroup.trim().length() == 0) {
            changeType = ChangeType.Deleted;
        }
        if (discipline == null || discipline.trim().length() == 0) {
            changeType = ChangeType.Deleted;
        }
        update(new TimeChangedEvent(timeChangedEvent.id(),
                                    changeType,
                                    timeChangedEvent.competition(),
                                    timeChangedEvent.acronym(),
                                    timeChangedEvent.eventType(),
                                    timeChangedEvent.name(),
                                    timeChangedEvent.club(),
                                    timeChangedEvent.nationality(),
                                    agegroup,
                                    timeChangedEvent.gender(),
                                    discipline,
                                    timeChangedEvent.timeInMillis()));
    }

    @NotNull
    private static TimeChangedEvent mapToTimeChangedEvent(EntryChangedEvent entryChangedEvent) {
        if (entryChangedEvent.type() != ChangeType.Deleted) {
            log.info("Event: {}", entryChangedEvent);
        }
        ChangeType changeType = entryChangedEvent.type();
        CompetitionDto competition = entryChangedEvent.competition();
        EventDto event = entryChangedEvent.event();
        EntryDto entry = entryChangedEvent.entry();

        if (event.inputValueType() == InputValueType.Rank) {
            changeType = ChangeType.Deleted;
        }
        if (entry.penalties() != null && entry.penalties().length > 0) {
            changeType = ChangeType.Deleted;
        }
        if (entry.timeInMillis() <= 0) {
            changeType = ChangeType.Deleted;
        }
        return new TimeChangedEvent(entry.id(),
                                    changeType,
                                    competition.name(),
                                    competition.acronym(),
                                    event.eventType(),
                                    entry.name(),
                                    entry.club(),
                                    entry.nationality(),
                                    event.agegroup(),
                                    event.gender(),
                                    event.discipline(),
                                    entry.timeInMillis());
    }

    private void update(TimeChangedEvent time) {
        if (time.changeType() == ChangeType.Deleted) {
            if (time.timeInMillis() > 0) {
                log.info("Deleting time: {}", time);
            }
            repository.deleteById(time.id());
            return;
        }
        TimeEntity entity = repository.findById(time.id()).orElseGet(() -> new TimeEntity(time.id()));
        if (entity.toDto().equals(time.toDto())) {
            log.info("Already up to date: {}", time);
            return;
        }
        log.info("Saving time: {}", time);
        entity.updateFrom(time.toDto());
        repository.save(entity);
    }

    public TimeDto[] findTimes(TimesQuery query) {
        TimeEntity[] entities;
        if (query.getDiscipline().length() == 0) {
            entities = repository.findAllByEventTypeAndAgegroupAndGender(query.getEventType(),
                                                                         query.getAgegroup(),
                                                                         query.getGender());
        } else {
            entities = repository.findAllByEventTypeAndAgegroupAndGenderAndDiscipline(query.getEventType(),
                                                                                      query.getAgegroup(),
                                                                                      query.getGender(),
                                                                                      query.getDiscipline());
        }
        return Arrays.stream(entities).map(TimeEntity::toDto).sorted(Comparator.comparingInt(TimeDto::timeInMillis)).toArray(TimeDto[]::new);
    }
}
