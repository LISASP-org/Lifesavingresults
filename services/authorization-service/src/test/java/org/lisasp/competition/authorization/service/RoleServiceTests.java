package org.lisasp.competition.authorization.service;

import org.junit.jupiter.api.Test;
import org.lisasp.competition.base.api.exception.CouldNotInitializeException;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleServiceTests {

    @Test
    void empty() throws CouldNotInitializeException {
        RoleService service =
                new RoleService().loadRoles(inputStreamOf("emptyRoles.yaml"));

        assertNotNull(service);
    }

    @Test
    void singleRoleSingleRight() throws CouldNotInitializeException {
        RoleService service = new RoleService().loadRoles(inputStreamOf("singleRoleSingleRight.yaml"));

        assertNotNull(service);
    }

    @Test
    void singleRoleMultipleRights() throws CouldNotInitializeException {
        RoleService service = new RoleService().loadRoles(inputStreamOf("singleRoleMultipleRights.yaml"));

        assertNotNull(service);
    }

    @Test
    void multipleRolesMultipleRights() throws CouldNotInitializeException {
        RoleService service = new RoleService().loadRoles(inputStreamOf("multipleRolesMultipleRights.yaml"));

        assertNotNull(service);
    }

    private static InputStream inputStreamOf(String filename) {
        return RoleServiceTests.class.getResourceAsStream("/" + filename);
    }
}
