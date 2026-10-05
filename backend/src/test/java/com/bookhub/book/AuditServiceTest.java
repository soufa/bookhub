package com.bookhub.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.IllegalTransactionStateException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuditServiceTest {

    @Autowired
    private AuditService auditService;

    @Test
    void mandatory_should_throw_without_transaction() {
        assertThrows(IllegalTransactionStateException.class,
                () -> auditService.assertInTransaction());
    }

    @Test
    void requires_new_should_work_without_existing_transaction() {
        assertDoesNotThrow(() -> auditService.recordEvent("test-event"));
    }
}