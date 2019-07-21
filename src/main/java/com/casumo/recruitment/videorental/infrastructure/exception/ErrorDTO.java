package com.casumo.recruitment.videorental.infrastructure.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class ErrorDTO {
    private String message;
    private Map<String, String> params;
}
