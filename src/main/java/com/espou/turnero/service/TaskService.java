package com.espou.turnero.service;

import com.espou.turnero.storage.TaskDTO;
import com.espou.turnero.storage.TaskRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Flux<TaskDTO> getAllTasks() {
        return taskRepository.findAll();
    }

    public Mono<TaskDTO> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public Mono<TaskDTO> getTaskByInternalId(String internalId) {
        return taskRepository.findByInternalId(internalId);
    }

    public Mono<TaskDTO> createTask(TaskDTO taskDTO) {
        return taskRepository.save(taskDTO);
    }

    public Mono<TaskDTO> updateTask(String id, TaskDTO taskDTO) {
        return taskRepository.updateById(id, taskDTO);
    }

    public Mono<TaskDTO> updateTaskByInternalId(String internalId, TaskDTO taskDTO) {
        return taskRepository.findByInternalId(internalId)
                .switchIfEmpty(Mono.error(new NoSuchElementException(internalId + " Task not found")))
                .flatMap(existingTask -> {
                    taskDTO.setId(existingTask.getId());
                    return taskRepository.updateById(existingTask.getId(), taskDTO);
                });
    }

    public Mono<Void> deleteTask(String id) {
        return taskRepository.deleteById(id);
    }

    public Mono<Void> deleteTaskByInternalId(String internalId) {
        return taskRepository.deleteByInternalId(internalId);
    }
}