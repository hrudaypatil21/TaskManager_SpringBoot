package com.hruday.TaskManager.Service;


import com.hruday.TaskManager.DTO.TaskDTO.CreateTaskDTO;
import com.hruday.TaskManager.DTO.TaskDTO.TaskResponseDTO;
import com.hruday.TaskManager.DTO.TaskDTO.UpdateTaskDTO;
import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.TaskRepository;
import com.hruday.TaskManager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  TaskRepository taskRepository;

    @Transactional
    public TaskResponseDTO createTask(CreateTaskDTO createTaskDTO) {
        User assignedBy = userRepository.findByEmpId(createTaskDTO.getAssignedById())
                .orElseThrow(() -> new RuntimeException("Assigner not found"));

        User assignedTo = userRepository.findByEmpId(createTaskDTO.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));

        if (!assignedBy.getRoles().contains(User.Role.ROLE_ADMIN)) {
            if (!assignedBy.getEmpId().equals(assignedTo.getEmpId())) {
                throw new RuntimeException("Only admins can assign tasks to others");
            }
        }

        if(createTaskDTO.getDueDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Due date must be on or after the current time");
        }

        Task task = new Task();
        task.setTitle(createTaskDTO.getTitle());
        task.setDescription(createTaskDTO.getDescription());
        task.setStatus(createTaskDTO.getStatus());
        task.setDueDate(createTaskDTO.getDueDate());
        task.setAssignedBy(assignedBy);
        task.setAssignedTo(assignedTo);
        Task savedTask = taskRepository.save(task);



        return new TaskResponseDTO(savedTask);

    }

    @Transactional
    public TaskResponseDTO updateTask(UpdateTaskDTO updateTaskDTO) {
        Task task = taskRepository.findById(updateTaskDTO.getId())
                .orElseThrow(() -> new RuntimeException("Task not found with id " + updateTaskDTO.getId()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if(updateTaskDTO.getAssignedToId() != null) {
            User assignedTo = userRepository.findByEmpId(updateTaskDTO.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + updateTaskDTO.getAssignedToId()));
            task.setAssignedTo(assignedTo);

            System.out.println("Task assigned by: " + currentUser.getEmpId());
        }

        if (updateTaskDTO.getTitle() != null) {
            task.setTitle(updateTaskDTO.getTitle());
        }

        if (updateTaskDTO.getDescription() != null) {
            task.setDescription(updateTaskDTO.getDescription());
        }

        if (updateTaskDTO.getDueDate() != null) {
            task.setDueDate(updateTaskDTO.getDueDate());
        }

        if (updateTaskDTO.getStatus() != null) {
            task.setStatus(updateTaskDTO.getStatus());
        }

        Task updatedTask = taskRepository.save(task);

        return new TaskResponseDTO(updatedTask);
    }

    @Transactional
    public TaskResponseDTO deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);

        return new TaskResponseDTO(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByUserId(User user) {
        return taskRepository.findByAssignedToId(user.getId());
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTaskByUserId(String empId) {
        List<Task> tasks = taskRepository.findByAssignedToEmpId(empId);
        if(tasks.isEmpty()) {
            throw new RuntimeException("No tasks found");
        }

        return tasks;
    }

    @Transactional
    public int getTaskCountByStatus(Authentication authentication, String statusStr) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }

        User user = (User) authentication.getPrincipal();

        Task.Status status;
        try {
            status = Task.Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid task status: " + statusStr);
        }

        return taskRepository.countByAssignedToAndStatus(user, status);
    }
}
