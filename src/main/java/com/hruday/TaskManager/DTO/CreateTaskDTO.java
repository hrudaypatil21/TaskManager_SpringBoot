package com.hruday.TaskManager.DTO;

import com.hruday.TaskManager.Entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDTO {

    private String title;
    private String description;
    private String assignedTo;
    private Task.Status status;
    private LocalDate dueDate;

}
