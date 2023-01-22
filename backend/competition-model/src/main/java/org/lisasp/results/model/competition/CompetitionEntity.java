package org.lisasp.results.model.competition;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Value;
import org.lisasp.basics.spring.jpa.BaseEntity;
import org.lisasp.results.api.Event;
import org.lisasp.results.model.EventEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class CompetitionEntity extends BaseEntity {
    @Column(length = 40, nullable = false)
    private String name;
    @Column(length = 16)
    private String acronym;
    @Column(name = "from_date")
    private LocalDate from;
    @Column(name = "till_date")
    private LocalDate till;
    @OneToMany(mappedBy="competition", fetch = FetchType.LAZY)
    private List<EventEntity> events;
}
