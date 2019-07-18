package com.casumo.recruitment.videorental

import com.casumo.recruitment.videorental.infrastructure.RestControllerAdvice
import com.casumo.recruitment.videorental.sharedkernel.time.TimeProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [BaseConfiguration.class])
class IntegrationSpec extends Specification {

    @Autowired
    protected RestControllerAdvice restControllerAdvice;

    @Autowired
    protected TimeProvider timeProvider;

    protected static final String buildJson(Object object) {
        return TestUtils.mapToJson(object);
    }

    protected static final <T> T buildObject(String json, Class<T> clazz) {
        return TestUtils.mapToObject(json, clazz);
    }

    @Configuration
    static class BaseConfiguration {

        @Bean
        TimeProvider timeProvider() {
            return new TimeProvider();
        }

        @Bean
        RestControllerAdvice restControllerAdvice() {
            return new RestControllerAdvice();
        }

    }
}
