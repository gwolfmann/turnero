package com.espou.turnero.handler;

import com.espou.turnero.processor.ResourcePipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class ResourceHandler {

    private final ResourcePipeline resourcePipeline;

    @Autowired
    public ResourceHandler(ResourcePipeline resourcePipeline) {
        this.resourcePipeline = resourcePipeline;
    }


    @Bean
    public RouterFunction<ServerResponse> resourceRouter() {
        return route(GET("/resources"), resourcePipeline::getResourceList)
                .andRoute(GET("/resources/{id}"), resourcePipeline::getResourceSingle)
                .andRoute(GET("/resources/internal/{internalId}"), resourcePipeline::getResourceSingle);
    }
}
