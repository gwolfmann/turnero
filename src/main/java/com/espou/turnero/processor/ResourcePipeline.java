package com.espou.turnero.processor;


import com.espou.turnero.authentication.JwtValidationUtil;
import com.espou.turnero.model.Resource;
import com.espou.turnero.service.ResourceService;
import com.espou.turnero.storage.ResourceDTO;
import com.espou.turnero.storage.ResourceMapper;
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
public class ResourcePipeline {

    private final Pipeline<ResourceDTO, Resource,Resource> singleReadPipeline;
    private final Pipeline<List<ResourceDTO>, List<Resource>,List<Resource>> listReadPipeline;
    private final Pipeline<ResourceDTO,ResourceDTO,ResourceDTO> singleWritePipeline;

    private final Logger logger = LoggerFactory.getLogger(ResourcePipeline.class);

    private final ResourceService resourceService;
    private final JwtValidationUtil jwtValidationUtil;
    @Autowired
    public ResourcePipeline(ResourceService resourceService, JwtValidationUtil jwtValidationUtil){
        this.resourceService = resourceService;
        this.jwtValidationUtil = jwtValidationUtil;
        singleReadPipeline = singleReadPipelineBuilder();
        listReadPipeline = listReadPipelineBuilder();
        singleWritePipeline = writePipelineBuilder();
    }

    public Mono<ServerResponse> getResourceList(ServerRequest serverRequest) {
        logger.info("Received GET request for resource list");
        return listReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> getResourceSingle(ServerRequest serverRequest) {
        return singleReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> writeSingleResource(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> deleteResource(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }
    private Mono<List<Resource>> mapListToResource(List<ResourceDTO> list){
        return Mono.just(list.stream()
            .map(ResourceMapper::toEntity)
            .collect(Collectors.toList()));
    }

    private Pipeline<List<ResourceDTO>, List<Resource>,List<Resource>> listReadPipelineBuilder(){
        return Pipeline.<List<ResourceDTO>, List<Resource>,List<Resource>>builder()
                .validateRequest(jwtValidationUtil::validateJwtToken)
                .validateBody(Pipeline::noOp)
                .storageOp(x -> resourceService.getAllResources().collectList())
                .boProcessor(this::mapListToResource)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<ResourceDTO, Resource, Resource> singleReadPipelineBuilder(){
        return Pipeline.<ResourceDTO, Resource, Resource>builder()
                .validateRequest(jwtValidationUtil::validateJwtToken)
                .validateBody(Pipeline::noOp)
                .storageOp(this::getSingleResource)
                .boProcessor(x -> Mono.just(ResourceMapper.toEntity(x)))
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<ResourceDTO,ResourceDTO, ResourceDTO> writePipelineBuilder(){
        return Pipeline.<ResourceDTO,ResourceDTO, ResourceDTO>builder()
                .validateRequest(jwtValidationUtil::validateJwtToken)
                .validateBody(this::validateBody)
                .storageOp(this::writeResource)
                .boProcessor(Pipeline::noOp)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }
    private Mono<ResourceDTO> getSingleResource(ServerRequest serverRequest){
        Map<String,String> vars = serverRequest.pathVariables();
        if (vars.containsKey("id")) {
            logger.info("Received GET request for resource with Id: {}", vars.get("id"));
            return resourceService.getResourceById(vars.get("id"));
        }
        if (vars.containsKey("internalId")) {
            logger.info("Received GET request for resource with internalId: {}", vars.get("internalId"));
            return resourceService.getResourceByInternalId(vars.get("internalId"));
        }
        return Mono.empty();
    }
    private Mono<ResourceDTO> writeResource(ServerRequest serverRequest){
        Map<String,String> vars = serverRequest.pathVariables();
        if (vars.containsKey("internalId")) {
            String internalId = vars.get("internalId");
            if (serverRequest.method().equals(HttpMethod.DELETE)){
                logger.info("Received DELETE request for resource with internalId: {}", internalId);
                return resourceService.deleteResource(internalId);
            } else{
                logger.info("Received PUT request for resource with internalId: {}", internalId);
                return serverRequest.bodyToMono(Resource.class)
                        .flatMap(x -> Mono.just(ResourceMapper.toDto(x)))
                        .flatMap(x -> resourceService.updateResource(x,internalId));
            }
        } else {
            logger.info("Received POST request for resource ");
            return serverRequest.bodyToMono(Resource.class)
                    .flatMap(x -> Mono.just(ResourceMapper.toDto(x)))
                    .flatMap(resourceService::writeResource);

        }
    }

    private Mono<ServerRequest> validateBody(ServerRequest serverRequest) {
        if (serverRequest.method().equals(HttpMethod.DELETE)) {
            return Mono.just(serverRequest);
        }
        return serverRequest.bodyToMono(ResourceDTO.class)
                .flatMap(resourceDTO -> {
                    if (isResourceValid(resourceDTO)) {
                        return Mono.just(ServerRequest.from(serverRequest).body(Pipeline.asJson(resourceDTO)).build());
                    } else {
                        return Mono.error(new RuntimeException("No proper body in the resource Post " + resourceDTO));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Empty request body")));
    }

    private boolean isResourceValid(ResourceDTO resource) {
        return resource.getName() != null && !resource.getName().isEmpty()
                && resource.getTimeline() != null
                && resource.getInternalId() != null && !resource.getInternalId().isEmpty();
    }

}
