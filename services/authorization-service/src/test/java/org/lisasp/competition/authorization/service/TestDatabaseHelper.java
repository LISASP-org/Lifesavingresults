package org.lisasp.competition.authorization.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.authorization.service.users.UserRoleRepository;
import org.springframework.test.context.transaction.TestTransaction;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
class TestDatabaseHelper {

    private final UserRoleRepository repository;

    void prepare() {
    }

    void cleanup() {
        TestTransaction.flagForRollback();
        TestTransaction.end();

        TestTransaction.start();
        repository.deleteAll();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        assertEquals(0, repository.count());
    }

    public void commit() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }
}
