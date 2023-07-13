package com.espou.turnero.processor;

import com.espou.turnero.model.Ping;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Getter
public class PingPipeline {

    private final Pipeline<Ping,Ping,Ping> pipeline;

    public PingPipeline(){
        pipeline = Pipeline.<Ping,Ping,Ping>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(x ->  Mono.just(new Ping("pong")))
                .boProcessor(Pipeline::noOp)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    public Mono<ServerResponse> executeToServerResponse(ServerRequest serverRequest) {
        return pipeline.executeToServerResponse(serverRequest);
    }
}
