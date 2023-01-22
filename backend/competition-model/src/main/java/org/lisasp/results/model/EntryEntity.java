package org.lisasp.results.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.lisasp.basics.spring.jpa.BaseEntity;
import org.lisasp.results.api.Penalty;
import org.lisasp.results.api.Start;
import org.lisasp.results.api.Swimmer;

@Entity
@Data
public class EntryEntity extends BaseEntity {
    @ManyToOne
    private EventEntity event;
    private String number;
    private String name;
    private String club;
    private String nationality;
    private int timeInMillis;
    private int placeInHeat;
    @Type(JsonType.class)
    @Column(columnDefinition = "text")
    private Penalty[] penalties;
    @Type(JsonType.class)
    @Column(columnDefinition = "text")
    private Swimmer[] swimmer;
    @Type(JsonType.class)
    @Column(columnDefinition = "text")
    private Start start;
}
