package com.bookhub.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfig.class, LifecycleBean.class })
class AppConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void should_provide_clock_bean() {
        Clock clock = context.getBean(Clock.class);
        assertNotNull(clock);
    }

    @Test
    void should_provide_application_name_bean() {
        String name = context.getBean("applicationName", String.class);
        assertEquals("BookHub", name);
    }

    @Test
    void should_create_lifecycle_bean_and_call_post_construct() {
        LifecycleBean bean = context.getBean(LifecycleBean.class);
        assertTrue(bean.isStarted(), "@PostConstruct doit avoir été appelé");
    }
}