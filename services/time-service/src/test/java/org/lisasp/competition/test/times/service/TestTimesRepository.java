package org.lisasp.competition.test.times.service;

import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.times.service.TimeEntity;
import org.lisasp.competition.times.service.TimesRepository;

public class TestTimesRepository extends TestDoubleCrudRepository<TimeEntity> implements TimesRepository {
    @Override
    public TimeEntity[] findAllByEventTypeAndAgegroupAndGenderAndDiscipline(EventType eventType, String agegroup, Gender gender, String discipline) {
        return this.entries.values()
                           .stream()
                           .filter(e -> e.getEventType() == eventType && e.getAgegroup().equals(agegroup) && e.getGender() == gender && e.getDiscipline().equals(discipline))
                           .toArray(TimeEntity[]::new);
    }

    @Override
    public TimeEntity[] findAllByEventTypeAndAgegroupAndGender(EventType eventType, String agegroup, Gender gender) {
        return this.entries.values()
                           .stream()
                           .filter(e -> e.getEventType() == eventType && e.getAgegroup().equals(agegroup) && e.getGender() == gender)
                           .toArray(TimeEntity[]::new);
    }
}
