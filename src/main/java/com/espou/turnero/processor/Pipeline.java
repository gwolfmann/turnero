package com.espou.turnero.processor;

import com.espou.turnero.response.CustomResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Builder
public class Pipeline<RAW,BO,DTO> {

    public static <BO> Mono<BO> noOp(BO bo) {return Mono.just(bo);}
/*
    public static String responseAsJson(String s) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.createObjectNode().put("response", s);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
 */
    protected final Function<ServerRequest, Mono<ServerRequest>> validateRequest;
    protected final Function<ServerRequest, Mono<ServerRequest>> validateBody;
    protected final Function<ServerRequest, Mono<RAW>> storageOp;
    protected final Function<RAW, Mono<BO>> boProcessor;
    protected final Function<BO, Mono<DTO>> presenter;
    protected final Function<Throwable,Mono<DTO>> handleErrorResponse;

    public Mono<DTO> execute(ServerRequest serverRequest){
        return Mono.just(serverRequest)
                .flatMap(validateRequest)
                .flatMap(validateBody)
                .flatMap(storageOp)
                .flatMap(boProcessor)
                .flatMap(presenter)
                .onErrorResume(handleErrorResponse);
    }

    public <DTO> Mono<ServerResponse> executeToServerResponse(ServerRequest serverRequest){
        return execute(serverRequest)
                .map(dto -> CustomResponse.<DTO>builder()
                    .data((DTO) dto)
                    .httpStatus(HttpStatus.OK)
                    .requestPath(serverRequest.requestPath().toString())
                    .className(dto.getClass().getSimpleName())
                    .build())
                .flatMap(x -> ServerResponse.ok().bodyValue(x));
    }

}
