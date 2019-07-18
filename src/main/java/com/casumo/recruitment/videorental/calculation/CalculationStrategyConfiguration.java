package com.casumo.recruitment.videorental.calculation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CalculationStrategyConfiguration {

    @Bean
    public InitialCalculationStrategy newReleaseFilmCalculationStrategy(){
        return new NewReleaseCalculationStrategy();
    }


    @Bean
    public InitialCalculationStrategy oldFilmCalculationStrategy(){
        return new OldFilmCalculationStrategy();
    }


    @Bean
    public InitialCalculationStrategy regularFilmCalculationStrategy(){
        return new RegularFilmCalculationStrategy();
    }
}
