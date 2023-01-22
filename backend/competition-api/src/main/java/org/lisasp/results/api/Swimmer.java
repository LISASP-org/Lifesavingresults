package org.lisasp.results.api;

import lombok.Value;
import org.lisasp.results.api.type.Sex;

@Value
public class Swimmer {
    private String startnumber;
    private String firstName;
    private String lastName;
    private String organization;
    private Sex sex;
    private short yearOfBirth;
}
