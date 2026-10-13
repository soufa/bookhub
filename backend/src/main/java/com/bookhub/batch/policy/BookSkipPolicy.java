package com.bookhub.batch.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.dao.DuplicateKeyException;

/**
 * Politique de skip personnalisée :
 * - Skip les DuplicateKeyException (ISBN déjà en DB) jusqu'à 50 fois
 * - Ne skip PAS les autres exceptions → le job échoue
 */
public class BookSkipPolicy implements SkipPolicy {

    private static final Logger log = LoggerFactory.getLogger(BookSkipPolicy.class);
    private static final int MAX_DUPLICATE_SKIPS = 50;

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        if (skipCount >= MAX_DUPLICATE_SKIPS) {
            log.error("Nombre max de skips dépassé ({})", MAX_DUPLICATE_SKIPS);
            throw new SkipLimitExceededException(MAX_DUPLICATE_SKIPS, t);
        }

        if (t instanceof DuplicateKeyException) {
            log.warn("Skip : doublon ISBN détecté ({})", t.getMessage());
            return true;
        }

        // Ne pas skip les autres erreurs
        return false;
    }
}