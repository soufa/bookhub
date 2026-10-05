package com.bookhub.book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    /**
     * REQUIRES_NEW : suspend la transaction existante, crée une nouvelle transaction.
     * Si le caller rollback, l'audit est quand même persisté.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordEvent(String event) {
        log.info("Auditing event: {}", event);
        // En production : repository.save(new AuditLog(event));
    }

    /**
     * MANDATORY : doit être appelé dans une transaction existante.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void assertInTransaction() {
        log.info("OK, on est dans une transaction");
    }
}