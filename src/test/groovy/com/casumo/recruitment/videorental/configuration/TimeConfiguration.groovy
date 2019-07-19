package com.casumo.recruitment.videorental.configuration

import com.casumo.recruitment.videorental.shared.time.TimeProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spock.mock.DetachedMockFactory

@Configuration
class TimeConfiguration {

    private DetachedMockFactory factory = new DetachedMockFactory()

    @Bean
    TimeProvider timeProvider() {
        return factory.Spy(TimeProvider)
    }
}
