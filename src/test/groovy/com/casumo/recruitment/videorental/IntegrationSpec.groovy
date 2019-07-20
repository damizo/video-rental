package com.casumo.recruitment.videorental


import com.casumo.recruitment.videorental.shared.time.TimeProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
class IntegrationSpec extends Specification {

    @Autowired
    protected GlobalRestControllerAdvice restControllerAdvice

    @Autowired
    protected MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    protected TimeProvider timeProvider;

    protected static final String buildJson(Object object) {
        return TestUtils.mapToJson(object)
    }

    protected static final <T> T buildObject(String json, Class<T> clazz) {
        return TestUtils.mapToObject(json, clazz)
    }

    @Configuration
    static class BaseConfiguration {

        @Bean
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            mappingJackson2HttpMessageConverter.setObjectMapper(new ObjectMapper().setPropertyNamingStrategy(
                    PropertyNamingStrategy.LOWER_CAMEL_CASE))
            return mappingJackson2HttpMessageConverter
        }

        @Bean
        GlobalRestControllerAdvice restControllerAdvice() {
            return new GlobalRestControllerAdvice();
        }

    }
}
