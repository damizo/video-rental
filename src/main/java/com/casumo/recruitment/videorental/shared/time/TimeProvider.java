package com.casumo.recruitment.videorental.shared.time;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimeProvider {

    public LocalDate today() {
        return LocalDate.now();
    }
}
