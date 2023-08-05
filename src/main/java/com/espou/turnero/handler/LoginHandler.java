package com.espou.turnero.handler;

import com.espou.turnero.processor.LoginPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class LoginHandler {

    private final LoginPipeline loginPipeline;

    @Autowired
    public LoginHandler(LoginPipeline loginPipeline) {
        this.loginPipeline = loginPipeline;
    }

    @Bean
    public RouterFunction<ServerResponse> loginRouter() {
        return route(POST("/api/auth/login"), loginPipeline::loginProcess);
    }
}
