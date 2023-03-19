package org.lisasp.competition.results.imports.rescue2022;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

class NameHelper {

    private static final String[] prefixes = new String[]{"Van De ", "Von Den ", "Von Der ", "Van ", "Von "};

    private static final String[] spacedPrefixes = Arrays.stream(prefixes).map(p -> " " + p).toArray(String[]::new);

    static String fixName(String name) {
        String[] parts = name.trim().split(" ");
        return replacePrefixes(Arrays.stream(parts)
                                     .filter(p -> !p.isBlank())
                                     .map(p -> StringUtils.capitalize(p.toLowerCase(Locale.ROOT)))
                                     .collect(Collectors.joining(" ")));
    }

    private static String replacePrefixes(String name) {
        for (String prefix : prefixes) {
            if (name.startsWith(prefix)) {
                name = prefix.toLowerCase(Locale.ROOT) + name.substring(prefix.length());
            }
        }
        for (String prefix : spacedPrefixes) {
            if (name.contains(prefix)) {
                name = name.replace(prefix, prefix.toLowerCase(Locale.ROOT));
            }
        }
        return name;
    }
}
