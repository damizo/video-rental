package com.casumo.recruitment.videorental;

import com.casumo.recruitment.videorental.infrastructure.DataContainer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VideoRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoRentalApplication.class, args);
    }


    @Bean
    public CommandLineRunner loadData(DataContainer dataContainer) {
        return (args) -> {
            dataContainer.initializeCustomers();
            dataContainer.initializeFilms();
        };
    }
}
