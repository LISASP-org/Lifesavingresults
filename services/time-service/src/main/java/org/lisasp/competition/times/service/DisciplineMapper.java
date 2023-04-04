package org.lisasp.competition.times.service;

import org.lisasp.competition.times.api.TimeChangedEvent;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DisciplineMapper {

    private final List<String> disciplines = List.of("4*50m Hindernisstaffel",
                                                     "4*25m Rückenlage ohne Armtätigkeit",
                                                     "4*25m Puppenstaffel",
                                                     "4*25m Gurtretterstaffel",
                                                     "4*50m Gurtretterstaffel",
                                                     "4*25m Rettungsstaffel",
                                                     "4*50m Rettungsstaffel",
                                                     "4*50m Freistilstaffel",
                                                     "100m Hindernisschwimmen",
                                                     "100m kombinierte Rettungsübung",
                                                     "100m Retten einer Puppe mit Flossen",
                                                     "100m Retten einer Puppe mit Flossen und Gurtretter",
                                                     "200m Hindernisschwimmen",
                                                     "200m Super Lifesaver",
                                                     "25m Schleppen einer Puppe",
                                                     "50m Flossenschwimmen",
                                                     "50m Freistil",
                                                     "50m Hindernisschwimmen",
                                                     "50m Kombiniertes Schwimmen",
                                                     "50m Retten einer Puppe",
                                                     "50m Retten einer Puppe mit Flossen");

    private final Map<String, String> lowercaseMappedDisciplines = disciplines.stream().collect(Collectors.toMap(d -> d.toLowerCase(Locale.ROOT), d -> d));

    public String execute(TimeChangedEvent event) {
        return validate(map(event));
    }

    private String map(TimeChangedEvent event) {
        String discipline = mapEnglishDisciplines(event);
        if (discipline != null) {
            return discipline;
        }
        return event.discipline();
    }

    private String validate(String discipline) {
        return lowercaseMappedDisciplines.get(discipline.toLowerCase(Locale.ROOT));
    }

    private String mapEnglishDisciplines(TimeChangedEvent event) {
        String discipline = event.discipline().toLowerCase(Locale.ENGLISH).trim();
        return switch (discipline) {
            case "100m manikin carry fins" -> "100m Retten einer Puppe mit Flossen";
            case "100m manikin tow fins" -> "100m Retten einer Puppe mit Flossen und Gurtretter";
            case "100m obstacle swim" -> "100m Hindernisschwimmen";
            case "100m rescue medley" -> "100m kombinierte Rettungsübung";
            case "200m obstacle swim" -> "200m Hindernisschwimmen";
            case "200m super lifesaver" -> "200m Super Lifesaver";
            case "4x25 manikin relay" -> "4*25m Puppenstaffel";
            case "4x50 lifesaver relay m-w" -> "4*50m Rettungsstaffel";
            case "4x50 medley relay" -> "4*50m Gurtretterstaffel";
            case "4x50 obstacle relay" -> "4*50m Hindernisstaffel";
            case "50m manikin carry" -> "50m Retten einer Puppe";
            default -> null;
        };
    }
}
