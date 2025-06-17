package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.DTO.TaskDTO.CreateTaskDTO;
import com.hruday.TaskManager.DTO.TaskDTO.TaskResponseDTO;
import com.hruday.TaskManager.DTO.TaskDTO.UpdateTaskDTO;
import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Service.TaskService;
import com.hruday.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

//    @PreAuthorize("hasRole('ROLE_ADMIN') or #empId == authentication.principal.username")
//    @GetMapping("/user/{empId}")
//    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable String empId) {
//        List<Task> tasks = taskService.getTasksByUserId(empId);
//        return ResponseEntity.ok(tasks);
//    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();
        return ResponseEntity.ok(allTasks);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #createTaskDTO.assignedToId == authentication.principal.username")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> createTask(@ModelAttribute CreateTaskDTO createTaskDTO) {
        try {
            TaskResponseDTO newTask = taskService.createTask(createTaskDTO);
            System.out.println("AssignedToId: " + createTaskDTO.getAssignedToId());
            System.out.println("Current User (empId): " + SecurityContextHolder.getContext().getAuthentication().getName());

            return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
        } catch (RuntimeException e) {
            System.out.println("AssignedToId: " + createTaskDTO.getAssignedToId());
            System.out.println("Current User (empId): " + SecurityContextHolder.getContext().getAuthentication().getName());
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }


    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #updateTaskDTO.assignedToId == authentication.principal.username")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> updateTask(@ModelAttribute UpdateTaskDTO updateTaskDTO) {
        try {
            TaskResponseDTO updatedTask = taskService.updateTask(updateTaskDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }


    @PreAuthorize("@taskSecurityService.canDeleteTask(authentication.principal.username, #taskId)")
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("#createTaskDTO.assignedToId == authentication.principal.username")
    @GetMapping("/count/status/{statusStr}")
    public ResponseEntity<Integer> getTaskCountByStatus(@PathVariable String statusStr, Authentication authentication) {
        authentication.getPrincipal();
        int count = taskService.getTaskCountByStatus(authentication, statusStr);
        return ResponseEntity.ok(count);}
}
