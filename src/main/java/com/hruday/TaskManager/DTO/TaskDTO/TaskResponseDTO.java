package com.hruday.TaskManager.DTO.TaskDTO;

import com.hruday.TaskManager.Entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String assignedBy;
    private String assignedTo;
    private Task.Status status;
    private LocalDateTime assignedDate;
    private LocalDateTime dueDate;

    public TaskResponseDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.assignedBy = task.getAssignedBy().getName();
        this.assignedTo = task.getAssignedTo().getName();
        this.status = task.getStatus();
        this.assignedDate = task.getAssignedDate();
        this.dueDate = task.getDueDate();
    }
}
