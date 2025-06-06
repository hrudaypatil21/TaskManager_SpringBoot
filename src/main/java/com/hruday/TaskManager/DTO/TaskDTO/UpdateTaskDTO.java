package com.hruday.TaskManager.DTO.TaskDTO;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDTO {

    private Long id;
    private String title;
    private String description;
    private String assignedToId;
    private Task.Status status;
    private LocalDateTime dueDate;

}
