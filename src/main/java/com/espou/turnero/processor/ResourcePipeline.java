package com.espou.turnero.processor;

import com.espou.turnero.model.Resource;
import com.espou.turnero.service.ResourceService;
import com.espou.turnero.storage.ResourceDTO;
import com.espou.turnero.storage.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResourcePipeline {

    private final Pipeline<List<ResourceDTO>, List<Resource>,List<Resource>> listPipeline;
    @Autowired
    private ResourceService resourceService;

    public ResourcePipeline(){
        resourceService = new ResourceService();
        listPipeline = Pipeline.<List<ResourceDTO>, List<Resource>,List<Resource>>builder()
            .validateRequest(Pipeline::noOp)
            .validateBody(Pipeline::noOp)
            .storageOp(x -> resourceService.getAllResources().collectList())
            .boProcessor(this::mapListToResource)
            .presenter(Pipeline::noOp)
            .handleErrorResponse(Mono::error)
            .build();
    }

    public Mono<ServerResponse> executeToServerResponse(ServerRequest serverRequest) {
        return listPipeline.executeToServerResponse(serverRequest);
    }

    private Mono<List<Resource>> mapListToResource(List<ResourceDTO> list){
        return Mono.just(list.stream()
            .map(ResourceMapper::toEntity)
            .collect(Collectors.toList()));
    }
}
