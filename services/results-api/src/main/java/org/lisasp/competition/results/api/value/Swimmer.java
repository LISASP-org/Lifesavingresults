package org.lisasp.competition.results.api.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.lisasp.competition.base.api.type.Sex;

public record Swimmer(String startNumber, String firstName, String lastName, Sex sex, short yearOfBirth) {
    @JsonCreator
    public Swimmer(@JsonProperty(value = "startNumber", required = true) String startNumber,
                   @JsonProperty(value = "firstName", required = true) String firstName,
                   @JsonProperty(value = "lastName", required = true) String lastName,
                   @JsonProperty(value = "sex", required = true) Sex sex,
                   @JsonProperty(value = "yearOfBirth", required = true) short yearOfBirth) {
        this.startNumber = startNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.yearOfBirth = yearOfBirth;
    }
}
