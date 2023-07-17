package com.espou.turnero.handler;

import com.espou.turnero.processor.ReceiverPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class ReceiverHandler {
    private final ReceiverPipeline receiverPipeline;

    @Autowired
    public ReceiverHandler(ReceiverPipeline receiverPipeline) {
        this.receiverPipeline = receiverPipeline;
    }

    @Bean
    public RouterFunction<ServerResponse> receiverRouter() {
        return route(GET("/receivers"), receiverPipeline::getReceiverList)
                .andRoute(GET("/receivers/{id}"), receiverPipeline::getReceiverSingle)
                .andRoute(GET("/tasks/internal/{internalId}"), receiverPipeline::getReceiverSingle)
                .andRoute(POST("/tasks"), receiverPipeline::writeSingleReceiver)
                .andRoute(PUT("/tasks/{internalId}"), receiverPipeline::writeSingleReceiver)
                .andRoute(DELETE("/tasks/{internalId}"), receiverPipeline::deleteReceiver);
    }

}
