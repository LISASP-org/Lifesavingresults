package org.lisasp.results.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.lisasp.basics.spring.jpa.BaseEntity;
import org.lisasp.results.model.EventEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

@Entity
@Getter
@Setter
@ToString(exclude = {"events"})
public class CompetitionEntity extends BaseEntity {
    @Column(length = 40, nullable = false)
    private String name;
    @Column(length = 16)
    private String acronym;
    @Column(length = 40)
    private String uploadId;
    @Column(name = "from_date")
    private LocalDate from;
    @Column(name = "till_date")
    private LocalDate till;
    @OneToMany(mappedBy="competition", fetch = FetchType.LAZY)
    private List<EventEntity> events;
}
