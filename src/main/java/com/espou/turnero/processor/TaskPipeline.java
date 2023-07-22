package com.espou.turnero.processor;

import com.espou.turnero.model.Task;
import com.espou.turnero.service.TaskService;
import com.espou.turnero.storage.TaskDTO;
import com.espou.turnero.storage.TaskMapper;
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
public class TaskPipeline {
    private final Pipeline<TaskDTO, Task, Task> singleReadPipeline;
    private final Pipeline<List<TaskDTO>, List<Task>, List<Task>> listReadPipeline;
    private final Pipeline<TaskDTO, TaskDTO, TaskDTO> singleWritePipeline;

    private final Logger logger = LoggerFactory.getLogger(TaskPipeline.class);

    private final TaskService taskService;

    @Autowired
    public TaskPipeline(TaskService taskService) {
        this.taskService = taskService;
        singleReadPipeline = singleReadPipelineBuilder();
        listReadPipeline = listReadPipelineBuilder();
        singleWritePipeline = writePipelineBuilder();
    }

    public Mono<ServerResponse> getTaskList(ServerRequest serverRequest) {
        logger.info("Received GET request for task list");
        return listReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> getTaskSingle(ServerRequest serverRequest) {
        return singleReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> writeSingleTask(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> deleteTask(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    private Mono<List<Task>> mapListToTask(List<TaskDTO> list) {
        return Mono.just(list.stream()
                .map(TaskMapper::toEntity)
                .collect(Collectors.toList()));
    }

    private Pipeline<List<TaskDTO>, List<Task>, List<Task>> listReadPipelineBuilder() {
        return Pipeline.<List<TaskDTO>, List<Task>, List<Task>>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(x -> taskService.getAllTasks().collectList())
                .boProcessor(this::mapListToTask)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<TaskDTO, Task, Task> singleReadPipelineBuilder() {
        return Pipeline.<TaskDTO, Task, Task>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(this::getSingleTask)
                .boProcessor(x -> Mono.just(TaskMapper.toEntity(x)))
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<TaskDTO, TaskDTO, TaskDTO> writePipelineBuilder() {
        return Pipeline.<TaskDTO, TaskDTO, TaskDTO>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(this::validateBody)
                .storageOp(this::writeTask)
                .boProcessor(Pipeline::noOp)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Mono<TaskDTO> getSingleTask(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("id")) {
            logger.info("Received GET request for task with Id: {}", vars.get("id"));
            return taskService.getTaskById(vars.get("id"));
        }
        if (vars.containsKey("internalId")) {
            logger.info("Received GET request for task with internalId: {}", vars.get("internalId"));
            return taskService.getTaskByInternalId(vars.get("internalId"));
        }
        return Mono.empty();
    }

    private Mono<TaskDTO> writeTask(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("internalId")) {
            String internalId = vars.get("internalId");
            if (serverRequest.method().equals(HttpMethod.DELETE)) {
                logger.info("Received DELETE request for task with internalId: {}", internalId);
                return taskService.deleteTask(internalId);
            } else {
                logger.info("Received PUT request for task with internalId: {}", internalId);
                return serverRequest.bodyToMono(Task.class)
                        .flatMap(x -> Mono.just(TaskMapper.toDto(x)))
                        .flatMap(x -> taskService.updateTask(x, internalId));
            }
        } else {
            logger.info("Received POST request for task");
            return serverRequest.bodyToMono(Task.class)
                    .flatMap(x -> Mono.just(TaskMapper.toDto(x)))
                    .flatMap(taskService::writeTask);
        }
    }
    private Mono<ServerRequest> validateBody(ServerRequest serverRequest) {
        if (serverRequest.method().equals(HttpMethod.DELETE)) {
            return Mono.just(serverRequest);
        }
        return serverRequest.bodyToMono(TaskDTO.class)
                .flatMap(taskDTO -> {
                    if (isTaskValid(taskDTO)) {
                        return Mono.just(ServerRequest.from(serverRequest).body(Pipeline.asJson(taskDTO)).build());
                    } else {
                        return Mono.error(new RuntimeException("No proper body in the task Post " + taskDTO));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Empty request body")));
    }

    private boolean isTaskValid(TaskDTO task) {
        return task.getName() != null && !task.getName().isEmpty()
                && task.getDuration() != null
                && task.getInternalId() != null && !task.getInternalId().isEmpty();
    }

}
