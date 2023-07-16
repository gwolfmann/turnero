package com.espou.turnero.handler;

import com.espou.turnero.processor.MeetPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class MeetHandler {
    private final MeetPipeline meetPipeline;

    @Autowired
    public MeetHandler(MeetPipeline meetPipeline) {
        this.meetPipeline = meetPipeline;
    }

    @Bean
    public RouterFunction<ServerResponse> meetRouter() {
        return route(GET("/meets"), meetPipeline::getMeetList)
                .andRoute(GET("/meets/{id}"), meetPipeline::getMeetSingle)
                .andRoute(GET("/meets/internal/{internalId}"), meetPipeline::getMeetSingle)
                .andRoute(POST("/meets"), meetPipeline::createMeet)
                .andRoute(PUT("/meets/{internalId}"), meetPipeline::updateMeet)
                .andRoute(DELETE("/meets/{internalId}"), meetPipeline::deleteMeet);
    }
}
