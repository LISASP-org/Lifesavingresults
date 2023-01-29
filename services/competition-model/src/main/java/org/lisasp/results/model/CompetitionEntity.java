package org.lisasp.results.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.lisasp.results.spring.jpa.BaseEntity;

import java.time.LocalDate;
import java.util.List;

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
