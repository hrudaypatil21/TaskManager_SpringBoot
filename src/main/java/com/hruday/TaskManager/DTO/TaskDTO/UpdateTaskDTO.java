package com.hruday.TaskManager.DTO.TaskDTO;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;

import java.time.LocalDateTime;

public class UpdateTaskDTO {

    private String title;
    private String description;
    private String assignedToId;
    private Task.Status status;
    private LocalDateTime dueDate;

}
