package com.espou.turnero.processor;

import com.espou.turnero.authentication.AuthenticationResponse;
import com.espou.turnero.authentication.AuthenticationService;
import com.espou.turnero.authentication.LoginCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class LoginPipeline {
    private final Pipeline<AuthenticationResponse, AuthenticationResponse, AuthenticationResponse> loginPipeline;

    private final Logger logger = LoggerFactory.getLogger(LoginPipeline.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public LoginPipeline(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        loginPipeline = loginPipelineBuilder();
    }

    public Mono<ServerResponse> loginProcess(ServerRequest serverRequest) {
        logger.info("Received login request");
        return loginPipeline.executeToServerResponse(serverRequest);
    }

    private Pipeline<AuthenticationResponse, AuthenticationResponse, AuthenticationResponse> loginPipelineBuilder() {
        return Pipeline.<AuthenticationResponse, AuthenticationResponse, AuthenticationResponse>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(this::loginValidation)
                .boProcessor(Pipeline::noOp)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Mono<AuthenticationResponse> loginValidation(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginCredentials.class)
                .flatMap(authenticationService::authenticate);
    }

}
