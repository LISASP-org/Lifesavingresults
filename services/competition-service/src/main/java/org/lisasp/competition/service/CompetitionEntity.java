package org.lisasp.competition.service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lisasp.basics.spring.jpa.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CompetitionEntity extends BaseEntity {
    @Column(length = 40, nullable = false)
    @NotNull
    private String name;
    @Column(length = 16, nullable = false)
    @NotNull
    private String acronym;
    @Column(name = "from_date", nullable = false)
    @NotNull
    private LocalDate from;
    @Column(name = "till_date", nullable = false)
    @NotNull
    private LocalDate till;

    public CompetitionEntity(String id) {
        super(id);
    }

    @Override
    public String toString() {
        return String.format("CompetitionEntity(id=%s, version=%d, name=%s, acronym=%s, from=%tF, till=%tF)", getId(), getVersion(), name, acronym, from, till);
    }
}
