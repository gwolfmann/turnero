package com.espou.turnero.service;

import com.espou.turnero.storage.TaskDTO;
import com.espou.turnero.storage.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Flux<TaskDTO> getAllTasks() {
        return taskRepository.findAll();
    }

    public Mono<TaskDTO> getTaskById(String id) {
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found Task by id " + id)));
    }

    public Mono<TaskDTO> getTaskByInternalId(String internalId) {
        return taskRepository.findByInternalId(internalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found Task by internalId " + internalId)));
    }

    public Mono<TaskDTO> writeTask(TaskDTO taskDTO) {
        return taskRepository.save(taskDTO);
    }

    public Mono<TaskDTO> updateTask(TaskDTO taskDTO, String internalId) {
        return taskRepository.findByInternalId(internalId)
                .flatMap(existingTask -> updateId(taskDTO, existingTask.getId()))
                .flatMap(taskRepository::save)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found to update Task by internalId " + internalId)));
    }

    public Mono<TaskDTO> deleteTask(String internalId) {
        return taskRepository.deleteByInternalId(internalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found to delete Task by internalId " + internalId)));
    }

    private Mono<TaskDTO> updateId(TaskDTO taskDTO, String id) {
        taskDTO.setId(id);
        return Mono.just(taskDTO);
    }

    private TaskDTO representsDeleted(TaskDTO taskDTO) {
        taskDTO.setName("deleted value");
        return taskDTO;
    }
}
