package org.lisasp.competition.results.service.imports.jauswertung;

import org.lisasp.competition.base.api.type.CourseType;

import java.util.Arrays;
import java.util.List;

public class CourseTypeConverter {
    public CourseType fromString(String description) {
        if (description == null) {
            return CourseType.Unknown;
        }
        List<Integer> values =
                Arrays.stream(description.replace(',', ' ').replace("\r\n", " ").replace("\n", " ").replace("\r",
                        " ").split(" ")).map(s -> s.endsWith("m") ? s.substring(0, s.length() - 1) : s).map(s -> {
            if (s.endsWith("m")) {
                s = s.substring(0, s.length() - 1);
            }
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException nfe) {
                return null;
            }
        }).distinct().filter(value -> value != null).toList();
        if (values.size() != 1) {
            return CourseType.Unknown;
        }
        return switch (values.get(0)) {
            case 25 -> CourseType.Short;
            case 50 -> CourseType.Long;
            default -> CourseType.Other;
        };
    }
}
