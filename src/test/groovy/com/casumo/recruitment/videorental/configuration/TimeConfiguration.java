package com.casumo.recruitment.videorental.configuration;

import com.casumo.recruitment.videorental.shared.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpSession;
import spock.mock.DetachedMockFactory;

import javax.servlet.http.HttpSession;

@Configuration
public class TimeConfiguration {

    private DetachedMockFactory factory = new DetachedMockFactory();

    @Bean
    TimeProvider timeProvider() {
        return factory.Spy(TimeProvider.class);
    }

    @Bean
    HttpSession httpSession() {
        return factory.Spy(MockHttpSession.class);
    }
}
