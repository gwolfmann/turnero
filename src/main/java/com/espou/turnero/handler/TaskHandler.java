package com.espou.turnero.handler;

import com.espou.turnero.processor.TaskPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class TaskHandler {

    private final TaskPipeline taskPipeline;

    @Autowired
    public TaskHandler(TaskPipeline taskPipeline) {
        this.taskPipeline = taskPipeline;
    }

    @Bean
    public RouterFunction<ServerResponse> taskRouter() {
        return route(GET("/tasks"), taskPipeline::getTaskList)
                .andRoute(GET("/tasks/{id}"), taskPipeline::getTaskSingle)
                .andRoute(GET("/tasks/internal/{internalId}"), taskPipeline::getTaskSingle)
                .andRoute(POST("/tasks"), taskPipeline::writeSingleTask)
                .andRoute(PUT("/tasks/{internalId}"), taskPipeline::writeSingleTask)
                .andRoute(DELETE("/tasks/{internalId}"), taskPipeline::deleteTask);
    }
}
