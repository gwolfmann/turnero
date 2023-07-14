package com.espou.turnero.response;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class CustomBadResponse {
    private String requestPath;
    private HttpStatus httpStatus;
    private String exceptionClassName;
    private String message;
    private StackTraceElement lastCall;
    @Builder.Default
    private LocalDateTime responseTime = LocalDateTime.now();
}