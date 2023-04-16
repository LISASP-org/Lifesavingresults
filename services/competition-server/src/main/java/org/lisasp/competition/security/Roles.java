package org.lisasp.competition.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Roles {
    CompetitionOrganizer("Competition Organizer"),
    CompetitionEditor("Competition Editor"),
    Administrator("Administrator");

    private final String name;

    public static Roles fromString(String role) {
        role = sanitize(role);
        for (Roles r : values()) {
            if (r.name.equalsIgnoreCase(role)) {
                return r;
            }
        }
        return null;
    }

    private static String sanitize(String role) {
        if (role == null) {
            return "";
        }
        return role.trim().replace('-', ' ');
    }
}
