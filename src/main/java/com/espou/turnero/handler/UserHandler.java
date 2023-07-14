package com.espou.turnero.handler;

import com.espou.turnero.processor.UserPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class UserHandler {

    private final UserPipeline userPipeline;

    @Autowired
    public UserHandler(UserPipeline userPipeline) {
        this.userPipeline = userPipeline;
    }

    @Bean
    public RouterFunction<ServerResponse> userRouter() {
        return route(GET("/users"), userPipeline::getUserList)
                .andRoute(GET("/users/{id}"), userPipeline::getUserSingle)
                .andRoute(GET("/users/internal/{internalId}"), userPipeline::getUserSingle)
                .andRoute(POST("/users"), userPipeline::writeSingleUser)
                .andRoute(PUT("/users/{internalId}"), userPipeline::writeSingleUser)
                .andRoute(DELETE("/users/{internalId}"), userPipeline::deleteUser);
    }
}
