package com.espou.turnero.handler;

import com.espou.turnero.processor.ProviderPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class ProviderHandler {

    private final ProviderPipeline providerPipeline;

    @Autowired
    public ProviderHandler(ProviderPipeline providerPipeline) {
        this.providerPipeline = providerPipeline;
    }

    @Bean
    public RouterFunction<ServerResponse> providerRouter() {
        return route(GET("/providers"), providerPipeline::getProviderList)
                .andRoute(GET("/providers/{id}"), providerPipeline::getProviderSingle)
                .andRoute(GET("/providers/internal/{internalId}"), providerPipeline::getProviderSingle)
                .andRoute(POST("/providers"), providerPipeline::writeSingleProvider)
                .andRoute(PUT("/providers/{internalId}"), providerPipeline::writeSingleProvider)
                .andRoute(DELETE("/providers/{internalId}"), providerPipeline::deleteProvider);
    }
}
