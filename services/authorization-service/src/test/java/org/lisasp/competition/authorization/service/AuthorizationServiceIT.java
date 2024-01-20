package org.lisasp.competition.authorization.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.lisasp.competition.api.authorization.Right;
import org.lisasp.competition.api.authorization.AuthorizationRequest;
import org.lisasp.competition.api.authorization.UserRole;
import org.lisasp.competition.authorization.service.users.UserRoleService;
import org.lisasp.competition.base.api.exception.CouldNotInitializeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
class AuthorizationServiceIT {

    private final AuthorizationRequest readResource15Request = new AuthorizationRequest("me", Right.builder().module("module1").name("id-15").build(), "read");

    @Autowired
    private TestDatabaseHelper testDatabaseHelper;

    @Autowired
    private UserRoleService userRoleService;

    private RoleService roleService;
    private AuthorizationService service;

    @BeforeEach
    void prepare() throws CouldNotInitializeException {
        roleService = new RoleService().loadRoles(AuthorizationServiceIT.class.getResourceAsStream("/authorizationServiceTestsRoles.yaml"));
        service = new AuthorizationService(roleService, userRoleService);
        testDatabaseHelper.prepare();
    }

    @AfterEach
    void cleanup() {
        testDatabaseHelper.cleanup();
    }

    @Test
    void requestAuthorizationWithoutAddingOne() {
        boolean actual = service.hasAuthorization(readResource15Request);

        assertFalse(actual);
    }

    @ParameterizedTest
    @CsvSource({"me,user,id-1,module1,create", "another,guest,id-13,module2,read", "we,editor,id-95,module3,update", "different,cleaner,id-1234567890,module4,delete"})
    void addOneAuthorization(String username, String role, String resourceId, String module, String action) {
        userRoleService.add(new UserRole(username, role, resourceId));
        testDatabaseHelper.commit();

        boolean actual = service.hasAuthorization(AuthorizationRequest.builder().username(username).resourceId(resourceId).right(new Right(module, action)).build());

        assertTrue(actual);
    }

    @ParameterizedTest
    @CsvSource({"me,user,id-1,module1,create", "another,guest,id-13,module2,read", "we,editor,id-95,module3,update", "different,cleaner,id-1234567890,module4,delete"})
    void addAndRemoveAuthorization(String username, String role, String resourceId, String module, String action) {
        userRoleService.add(new UserRole(username, role, resourceId));
        testDatabaseHelper.commit();
        userRoleService.remove(new UserRole(username, role, resourceId));
        testDatabaseHelper.commit();

        boolean actual = service.hasAuthorization(AuthorizationRequest.builder().username(username).resourceId(resourceId).right(new Right(module, action)).build());

        assertFalse(actual);
    }

    @ParameterizedTest
    @CsvSource({"me,user,id-1,module1,create", "another,guest,id-13,module2,read", "we,editor,id-95,module3,update", "different,cleaner,id-1234567890,module4,delete"})
    void addAuthorizationWithoutPredefinedRole(String username, String role, String resourceId, String module, String action) throws CouldNotInitializeException {
        roleService.loadRoles(AuthorizationServiceIT.class.getResourceAsStream("/emptyRoles.yaml"));

        userRoleService.add(new UserRole(username, role, resourceId));
        testDatabaseHelper.commit();

        boolean actual = service.hasAuthorization(AuthorizationRequest.builder().username(username).resourceId(resourceId).right(new Right(module, action)).build());

        assertFalse(actual);
    }
}
