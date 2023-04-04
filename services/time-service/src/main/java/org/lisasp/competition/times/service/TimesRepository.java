package org.lisasp.competition.times.service;

import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.springframework.data.repository.CrudRepository;

public interface TimesRepository extends CrudRepository<TimeEntity, String> {

    TimeEntity[] findAllByEventTypeAndAgegroupAndGenderAndDiscipline(EventType eventType, String agegroup, Gender gender, String discipline);

    TimeEntity[] findAllByEventTypeAndAgegroupAndGender(EventType eventType, String agegroup, Gender gender);
}
