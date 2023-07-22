package com.espou.turnero.processor;

import com.espou.turnero.model.User;
import com.espou.turnero.service.UserService;
import com.espou.turnero.storage.UserDTO;
import com.espou.turnero.storage.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserPipeline {

    private final Pipeline<UserDTO, User, User> singleReadPipeline;
    private final Pipeline<List<UserDTO>, List<User>, List<User>> listReadPipeline;
    private final Pipeline<UserDTO, UserDTO, UserDTO> singleWritePipeline;

    private final Logger logger = LoggerFactory.getLogger(UserPipeline.class);

    private final UserService userService;

    @Autowired
    public UserPipeline(UserService userService) {
        this.userService = userService;
        singleReadPipeline = singleReadPipelineBuilder();
        listReadPipeline = listReadPipelineBuilder();
        singleWritePipeline = writePipelineBuilder();
    }

    public Mono<ServerResponse> getUserList(ServerRequest serverRequest) {
        logger.info("Received GET request for user list");
        return listReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> getUserSingle(ServerRequest serverRequest) {
        return singleReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> writeSingleUser(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    private Mono<List<User>> mapListToUser(List<UserDTO> list) {
        return Mono.just(list.stream()
                .map(UserMapper::toEntity)
                .collect(Collectors.toList()));
    }

    private Pipeline<List<UserDTO>, List<User>, List<User>> listReadPipelineBuilder() {
        return Pipeline.<List<UserDTO>, List<User>, List<User>>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(x -> userService.getAllUsers().collectList())
                .boProcessor(this::mapListToUser)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<UserDTO, User, User> singleReadPipelineBuilder() {
        return Pipeline.<UserDTO, User, User>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(this::getSingleUser)
                .boProcessor(x -> Mono.just(UserMapper.toEntity(x)))
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<UserDTO, UserDTO, UserDTO> writePipelineBuilder() {
        return Pipeline.<UserDTO, UserDTO, UserDTO>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(this::validateBody)
                .storageOp(this::writeUser)
                .boProcessor(Pipeline::noOp)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Mono<UserDTO> getSingleUser(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("id")) {
            logger.info("Received GET request for user with Id: {}", vars.get("id"));
            return userService.getUserById(vars.get("id"));
        }
        if (vars.containsKey("internalId")) {
            logger.info("Received GET request for user with internalId: {}", vars.get("internalId"));
            return userService.getUserByInternalId(vars.get("internalId"));
        }
        return Mono.empty();
    }

    private Mono<UserDTO> writeUser(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("internalId")) {
            String internalId = vars.get("internalId");
            if (serverRequest.method().equals(HttpMethod.DELETE)) {
                logger.info("Received DELETE request for user with internalId: {}", internalId);
                return userService.deleteUser(internalId);
            } else {
                logger.info("Received PUT request for user with internalId: {}", internalId);
                return serverRequest.bodyToMono(User.class)
                        .flatMap(x -> Mono.just(UserMapper.toDto(x)))
                        .flatMap(x -> userService.updateUser(x, internalId));
            }
        } else {
            logger.info("Received POST request for user");
            return serverRequest.bodyToMono(User.class)
                    .flatMap(x -> Mono.just(UserMapper.toDto(x)))
                    .flatMap(userService::writeUser);
        }
    }

    private Mono<ServerRequest> validateBody(ServerRequest serverRequest){
        if (serverRequest.method().equals(HttpMethod.DELETE)) {
            return Mono.just(serverRequest);
        }
        return serverRequest.bodyToMono(UserDTO.class)
            .flatMap(userDTO -> {
                if (isUserValid(userDTO)) {
                    return Mono.just(ServerRequest.from(serverRequest).body(Pipeline.asJson(userDTO)).build());
                } else {
                    return Mono.error(new RuntimeException("No proper body in the user Post "+userDTO));
                }
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Empty request body")));
    }

    private boolean isUserValid(UserDTO user) {
        return user.getName() != null && !user.getName().isEmpty()
                && user.getEmail() != null && !user.getEmail().isEmpty()
                && user.getPassw() != null && !user.getPassw().isEmpty()
                && user.getInternalId() != null && !user.getInternalId().isEmpty();
    }

}
