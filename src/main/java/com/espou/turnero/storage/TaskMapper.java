package com.espou.turnero.storage;

import com.espou.turnero.model.Task;

public class TaskMapper {
    public static TaskDTO toDto(Task task) {
        return new TaskDTO(task.getId(), task.getName(), task.getDuration());
    }

    public static Task toEntity(TaskDTO taskDto) {
        return new Task(taskDto.getId(), taskDto.getName(), taskDto.getDuration());
    }
}
