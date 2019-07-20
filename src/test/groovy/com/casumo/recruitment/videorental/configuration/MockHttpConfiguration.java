package com.casumo.recruitment.videorental.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpSession;

@Configuration
public class MockHttpConfiguration {

    @Bean
    public MockHttpSession mockHttpSession() {
        MockHttpSession mock = Mockito.mock(MockHttpSession.class);
        Mockito.when(mock.getId()).thenReturn("1");
        return mock;
    }
}
