package com.espou.turnero.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtValidationUtil {

    @Autowired
    private JwtUtil jwtUtil;

    public Mono<ServerRequest> validateJwtToken(ServerRequest serverRequest) {
        String token = getTokenFromServerRequest(serverRequest);
        if (jwtUtil.validateToken(token)) {
            // Token is valid, return the original ServerRequest
            return Mono.just(serverRequest);
        } else {
            // Token is not valid, return an error
            return Mono.error(new RuntimeException("Invalid JWT token"));
        }
    }

    private String getTokenFromServerRequest(ServerRequest serverRequest) {
        List<String> authHeaders = serverRequest.headers().header("Authorization");
        if (!authHeaders.isEmpty()) {
            String authHeader = authHeaders.get(0);
            if (authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }
        return null;
    }
}
