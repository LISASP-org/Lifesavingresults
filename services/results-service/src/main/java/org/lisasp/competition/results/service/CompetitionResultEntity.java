package org.lisasp.competition.results.service;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lisasp.basics.spring.jpa.BaseEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CompetitionResultEntity extends BaseEntity {

    public CompetitionResultEntity(String id) {
        super(id);
    }

    @Column(length = 40, nullable = false)
    @NotNull
    private String name;
    @Column(length = 16, nullable = false)
    @NotNull
    private String acronym;
    @Column(length = 40, nullable = false)
    @NotNull
    private String uploadId;
    @Column(name = "from_date", nullable = false)
    @NotNull
    private LocalDate from;
    @Column(name = "till_date", nullable = false)
    @NotNull
    private LocalDate till;
    @OneToMany(mappedBy = "competition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<EventResultEntity> events;

    @Override
    public String toString() {
        return String.format("CompetitionResultEntity(id=%s, version=%d, name=%s, acronym=%s, uploadId=%s,from=%tF, till=%tF)",
                             getId(),
                             getVersion(),
                             name,
                             acronym,
                             uploadId,
                             from,
                             till);
    }
}
