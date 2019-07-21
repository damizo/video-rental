package com.casumo.recruitment.videorental.infrastructure.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class Error {
    private String message;
    private Map<String, String> params;
}
