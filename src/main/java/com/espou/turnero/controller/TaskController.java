package com.espou.turnero.controller;

import com.espou.turnero.service.TaskService;
import com.espou.turnero.storage.TaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<TaskDTO> getAllTasks() {
        logger.info("Received GET request for all tasks");
        return taskService.getAllTasks();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TaskDTO> getTaskById(@PathVariable String id) {
        logger.info("Received GET request for task with ID: {}", id);
        return taskService.getTaskByInternalId(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        logger.info("Received POST request to create a new task");
        return taskService.createTask(taskDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TaskDTO> updateTask(@PathVariable String id, @RequestBody TaskDTO taskDTO) {
        logger.info("Received PUT request to update task with ID: {}", id);
        return taskService.updateTaskByInternalId(id, taskDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTask(@PathVariable String id) {
        logger.info("Received DELETE request for task with ID: {}", id);
        return taskService.deleteTaskByInternalId(id);
    }

    @GetMapping(value = "/byMongoId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TaskDTO> getTaskByMongoId(@PathVariable String id) {
        logger.info("Received GET request for task with ID: {}", id);
        return taskService.getTaskById(id);
    }

    @PutMapping(value = "/byMongoId/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TaskDTO> updateTaskByMongoId(@PathVariable String id, @RequestBody TaskDTO taskDTO) {
        logger.info("Received PUT request to update task with ID: {}", id);
        return taskService.updateTask(id, taskDTO);
    }

    @DeleteMapping("/byMongoId/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTaskByMongoId(@PathVariable String id) {
        logger.info("Received DELETE request for task with ID: {}", id);
        return taskService.deleteTask(id);
    }
}
