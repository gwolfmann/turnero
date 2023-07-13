package com.espou.turnero.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

    @Bean
    RouterFunction<ServerResponse> pingRoutes(PingController pingController) {
        return RouterFunctions.route(GET("/ping").and(accept(MediaType.APPLICATION_JSON)), pingController::ping);
    }

    @Bean
    RouterFunction<ServerResponse> resourceRoutes(ResourceController resourceController) {
        return RouterFunctions.route(GET("/resources").and(accept(MediaType.APPLICATION_JSON)), resourceController::getAllResources);
    }

}