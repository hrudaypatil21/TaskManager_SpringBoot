package com.hruday.TaskManager.DTO.TaskDTO;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDTO {



    @NotNull
    private String title;

    private String description;
    @NotNull
    private String assignedById;
    @NotNull
    private String assignedToId;
    @NotNull
    private Task.Status status;
    @NotNull
    private LocalDateTime dueDate;

}
