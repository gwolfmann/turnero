package com.espou.turnero.processor;

import com.espou.turnero.response.CustomBadResponse;
import com.espou.turnero.response.CustomResponse;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Builder
public class Pipeline<RAW,BO,DTO> {

    public static <BO> Mono<BO> noOp(BO bo) {return Mono.just(bo);}
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
                .className(getClassName(dto))
                .responseCount(getResponseCount(dto))
                .build())
            .flatMap(x -> ServerResponse.ok().bodyValue(x))
            .onErrorResume(ex ->
                ServerResponse.badRequest().bodyValue(CustomBadResponse.builder()
                    .message(ex.getMessage())
                    .exceptionClassName(ex.getClass().getSimpleName())
                    .lastCall(ex.getStackTrace()[0])
                    .httpStatus(HttpStatus.OK)
                    .requestPath(serverRequest.requestPath().toString())
                    .build()));
    }

    private String getClassName(DTO dto){
        String className= dto.getClass().getSimpleName();
        if (className.equals("ArrayList")) {
            List list = (List) dto;
            if (list.size()>0) {
                className = className+'<'+list.get(0).getClass().getSimpleName()+'>';
            }
        }
        return className;
    }

    private Integer getResponseCount(DTO dto){
        Integer count = 0;
        if (dto!=null) {
            String className= dto.getClass().getSimpleName();
            if (className.equals("ArrayList")) {
                List list = (List) dto;
                count = list.size();
            } else {
                count = 1;
            }
        }
        return count;
    }
}
