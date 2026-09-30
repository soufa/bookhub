package com.bookhub.core;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @PostConstruct : appelé après l'injection des dépendances
 *
 * @PreDestroy : appelé avant la destruction du contexte
 *
 * @Component : stéréotype de base
 */
@Component
public class LifecycleBean {

    private static final Logger log = LoggerFactory.getLogger(LifecycleBean.class);

    private boolean started = false;

    @PostConstruct
    public void init() {
        log.info("LifecycleBean initializing");
        this.started = true;
    }

    @PreDestroy
    public void cleanup() {
        log.info("LifecycleBean shutting down");
        this.started = false;
    }

    public boolean isStarted() {
        return started;
    }
}