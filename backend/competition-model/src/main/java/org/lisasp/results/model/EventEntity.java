package org.lisasp.results.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.lisasp.basics.spring.jpa.BaseEntity;
import org.lisasp.results.api.Round;
import org.lisasp.results.api.type.EventTypes;
import org.lisasp.results.api.type.Genders;
import org.lisasp.results.api.type.InputValueTypes;
import org.lisasp.results.model.competition.CompetitionEntity;

import java.util.List;


@Entity
@Data
public class EventEntity extends BaseEntity {
    @ManyToOne
    private CompetitionEntity competition;
    private String agegroup;
    private EventTypes eventType;
    private Genders gender;
    private String discipline;
    @Type(JsonType.class)
    @Column(columnDefinition = "text")
    private Round round;
    private InputValueTypes inputValueType;
    @OneToMany(mappedBy="event", fetch = FetchType.LAZY)
    private List<EntryEntity> entries;
}
