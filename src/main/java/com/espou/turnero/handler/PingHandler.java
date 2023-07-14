package com.espou.turnero.handler;

import com.espou.turnero.controller.PingController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class PingHandler {

    @Bean
    RouterFunction<ServerResponse> pingRoutes(PingController pingController) {
        return RouterFunctions.route(GET("/ping").and(accept(MediaType.APPLICATION_JSON)), pingController::ping);
    }

}