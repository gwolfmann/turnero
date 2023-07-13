package com.espou.turnero.response;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class CustomResponse<T> {
    private String requestPath;
    private HttpStatus httpStatus;
    private T data;
    private String className;
    @Builder.Default
    private LocalDateTime responseTime = LocalDateTime.now();

}
