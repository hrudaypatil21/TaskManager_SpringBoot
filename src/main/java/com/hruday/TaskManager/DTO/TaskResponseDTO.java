package com.hruday.TaskManager.DTO;

import com.hruday.TaskManager.Entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {

    private String id;
    private String title;
    private String description;
    private String assignedBy;
    private String assignedTo;
    private Task.Status status;
    private LocalDateTime assignedDate;
    private LocalDateTime dueDate;

    public TaskResponseDTO(Task task) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignedBy = assignedBy;
        this.assignedTo = assignedTo;
        this.status = status;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
    }
}
