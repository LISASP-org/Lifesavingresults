package org.lisasp.results.service.imports.jauswertung.model;

import lombok.Data;
import lombok.Value;

@Data
public class Swimmer {
    private String startnumber;
    private String firstName;
    private String lastName;
    private String organization;
    private String sex;
    private short yearOfBirth;
}
