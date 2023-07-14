package com.espou.turnero.processor;


import com.espou.turnero.model.Resource;
import com.espou.turnero.service.ResourceService;
import com.espou.turnero.storage.ResourceDTO;
import com.espou.turnero.storage.ResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ResourcePipeline {

    private final Pipeline<ResourceDTO, Resource,Resource> singlePipeline;
    private final Pipeline<List<ResourceDTO>, List<Resource>,List<Resource>> listPipeline;

    private final Logger logger = LoggerFactory.getLogger(ResourcePipeline.class);

    private final ResourceService resourceService;
    @Autowired
    public ResourcePipeline(ResourceService resourceService){
        this.resourceService = resourceService;
        singlePipeline = singlePipelineBuilder();
        listPipeline = listPipelineBuilder();
    }

    public Mono<ServerResponse> getResourceList(ServerRequest serverRequest) {
        logger.info("Received GET request for resource list");
        return listPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> getResourceSingle(ServerRequest serverRequest) {
        return singlePipeline.executeToServerResponse(serverRequest);
    }

    private Mono<List<Resource>> mapListToResource(List<ResourceDTO> list){
        return Mono.just(list.stream()
            .map(ResourceMapper::toEntity)
            .collect(Collectors.toList()));
    }

    private Pipeline listPipelineBuilder(){
        return Pipeline.<List<ResourceDTO>, List<Resource>,List<Resource>>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(x -> resourceService.getAllResources().collectList())
                .boProcessor(this::mapListToResource)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline singlePipelineBuilder(){
        return Pipeline.< ResourceDTO, Resource, Resource>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(this::getSingleResource)
                .boProcessor(x -> Mono.just(ResourceMapper.toEntity(x)))
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
}
