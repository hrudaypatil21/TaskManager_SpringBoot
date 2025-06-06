package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.DTO.TaskDTO.CreateTaskDTO;
import com.hruday.TaskManager.DTO.TaskDTO.TaskResponseDTO;
import com.hruday.TaskManager.DTO.TaskDTO.UpdateTaskDTO;
import com.hruday.TaskManager.Service.TaskService;
import com.hruday.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody CreateTaskDTO createTaskDTO) {
        TaskResponseDTO newTask = taskService.createTask(createTaskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@RequestParam Long taskId, @RequestBody UpdateTaskDTO updateTaskDTO) {
        updateTaskDTO.setId(taskId);
        TaskResponseDTO updatedTask = taskService.updateTask(updateTaskDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedTask);
    }


}
