package com.espou.turnero.controller;

import com.espou.turnero.processor.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;


@RestController
@RequestMapping("/ping")
public class PingController {

    private final Pipeline<String,String,String> pipeline;
    private final Logger logger = LoggerFactory.getLogger(PingController.class);

    public PingController() {
        this.pipeline = Pipeline.<String,String,String>builder()
            .validateRequest(Pipeline::noOp)
            .validateBody(Pipeline::noOp)
            .storageOp(x ->  Mono.just("Pong"))
            .boProcessor(Pipeline::noOp)
            .presenter(x -> Mono.just(Pipeline.responseAsJson(x)))
            .handleErrorResponse(Mono::error)
            .build();
    }
    //@RequestMapping("/")
    public Mono<ServerResponse> ping(ServerRequest serverRequest) {
        logger.info("Received GET request for ping");
        return pipeline.executeToServerResponse(serverRequest);
    }
}
