package org.lisasp.competition.authorization.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.lisasp.competition.api.authorization.Right;
import org.lisasp.competition.api.authorization.Role;
import org.lisasp.competition.base.api.exception.CouldNotInitializeException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RoleService {

    private static final Role EmptyRole = new Role("", new Right[0]);

    private final ObjectMapper mapper;

    private Map<String, Role> roles = Map.of();

    public RoleService() {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
    }

    public RoleService loadRoles(InputStream input) throws CouldNotInitializeException {
        try {
            roles = Arrays.stream(mapper.readValue(input.readAllBytes(), Role[].class)).collect(Collectors.toMap(r -> r.name(), r -> r));
            return this;
        } catch (RuntimeException | IOException ex) {
            throw new CouldNotInitializeException("Roles", ex);
        }
    }

    public boolean hasAuthorization(String[] roleNames, Right right) {
        for (String name : roleNames) {
            Role role = roles.getOrDefault(name, EmptyRole);
            for (Right roleRight : role.rights()) {
                if (roleRight.equals(right)) {
                    return true;
                }
            }
        }
        return false;
    }
}
