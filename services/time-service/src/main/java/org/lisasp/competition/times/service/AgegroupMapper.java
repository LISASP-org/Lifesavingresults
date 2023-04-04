package org.lisasp.competition.times.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.times.api.TimeChangedEvent;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AgegroupMapper {

    private final Pattern AgegroupMasters = Pattern.compile("^[WM](\\d[05])-(\\d[49])$");

    private final Set<String> agegroups = Set.of("AK 12",
                                                 "AK 13/14",
                                                 "AK 15/16",
                                                 "AK 17/18",
                                                 "AK Offen",
                                                 "AK 25",
                                                 "AK 30",
                                                 "AK 35",
                                                 "AK 40",
                                                 "AK 45",
                                                 "AK 50",
                                                 "AK 55",
                                                 "AK 60",
                                                 "AK 65",
                                                 "AK 70",
                                                 "AK 75",
                                                 "AK 80",
                                                 "AK 85",
                                                 "AK 90",
                                                 "AK 95",
                                                 "AK 120",
                                                 "AK 140",
                                                 "AK 170",
                                                 "AK 200");

    public String execute(TimeChangedEvent event) {
        return validate(map(event));
    }

    @Nullable
    private String map(TimeChangedEvent event) {
        if (event.agegroup() == null) {
            return null;
        }
        String agegroup = mapEnglishIndividualMasters(event);
        if (agegroup != null) {
            return agegroup;
        }
        agegroup = mapEnglishLineThrowMasters(event);
        if (agegroup != null) {
            return agegroup;
        }
        agegroup = mapEnglishTeamMasters(event);
        if (agegroup != null) {
            return agegroup;
        }
        agegroup = mapEnglishOpen(event);
        if (agegroup != null) {
            return agegroup;
        }
        return event.agegroup();
    }

    private String mapEnglishTeamMasters(TimeChangedEvent event) {
        if (event.eventType() != EventType.Team) {
            return null;
        }
        return switch (event.agegroup().trim()) {
            case "W100-119", "M100-119" -> "AK 100";
            case "W120-139", "M120-139" -> "AK 120";
            case "W140-169", "M140-169" -> "AK 140";
            case "W170-199", "M170-199" -> "AK 170";
            default -> null;
        };
    }

    private String validate(String agegroup) {
        if (!agegroups.contains(agegroup)) {
            log.info("Unknown agegroup: {}", agegroup);
            return null;
        }
        return agegroup;
    }

    private String mapEnglishOpen(TimeChangedEvent event) {
        if (event.agegroup().equalsIgnoreCase("open")) {
            return "AK Offen";
        }
        return null;
    }

    private String mapEnglishLineThrowMasters(TimeChangedEvent event) {
        if (!event.discipline().equalsIgnoreCase("Line Throw")) {
            return null;
        }
        return mapEnglishMastersSingleCompetitorBased(event);
    }

    private String mapEnglishIndividualMasters(TimeChangedEvent event) {
        if (event.eventType() != EventType.Individual) {
            return null;
        }
        return mapEnglishMastersSingleCompetitorBased(event);
    }

    private String mapEnglishMastersSingleCompetitorBased(TimeChangedEvent event) {
        String agegroup = event.agegroup().trim();
        switch (event.gender()) {
            case Female: {
                if (!agegroup.startsWith("W")) {
                    return null;
                }
                break;
            }
            case Male: {
                if (!agegroup.startsWith("M")) {
                    return null;
                }
                break;
            }
            case Mixed: {
                if (!agegroup.startsWith("X")) {
                    return null;
                }
                break;
            }
            default: {
                return null;
            }
        }
        Matcher matcher = AgegroupMasters.matcher(agegroup);
        if (!matcher.matches()) {
            return null;
        }
        int min = Integer.parseInt(matcher.group(1));
        int max = Integer.parseInt(matcher.group(2));
        if (min + 4 != max) {
            return null;
        }
        if (min < 30) {
            return null;
        }
        return String.format("AK %d", min);
    }
}
