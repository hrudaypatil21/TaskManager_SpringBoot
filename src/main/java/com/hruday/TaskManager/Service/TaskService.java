package com.hruday.TaskManager.Service;


import com.hruday.TaskManager.DTO.CreateTaskDTO;
import com.hruday.TaskManager.DTO.TaskResponseDTO;
import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.TaskRepository;
import com.hruday.TaskManager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TaskService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public TaskService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public TaskResponseDTO createTask(CreateTaskDTO createTaskDTO) {
//        User user = userRepository.findByEmpId(createTaskDTO.getAssignedBy().getEmpId())
//                .orElseThrow(() -> new RuntimeException("User not found with this ID"));
//
//        if (user.getRole() == User.Role.ADMIN || Objects.equals(user.getEmpId(), createTaskDTO.getAssignedTo().getEmpId())) {
//
//        }

        if(createTaskDTO.getAssignedBy().getRole() != User.Role.ADMIN ||
                createTaskDTO.getAssignedBy().getRole() != createTaskDTO.getAssignedTo().getRole()
        ) {
            throw new RuntimeException("Only Admin can assign tasks to other users or users can assign tasks to themselves");
        }

        Task task = new Task();
        task.setTitle(createTaskDTO.getTitle());
        task.setDescription(createTaskDTO.getDescription());
        task.setStatus(createTaskDTO.getStatus());
        task.setDueDate(createTaskDTO.getDueDate());
        task.setAssignedBy(userRepository.findByEmpId(createTaskDTO.getAssignedBy().getEmpId())
                .orElseThrow(() -> new RuntimeException("User not found with this ID")));
        task.setAssignedTo(userRepository.findByEmpId(createTaskDTO.getAssignedTo().getEmpId())
                .orElseThrow(() -> new RuntimeException("User not found with this ID")));
        Task savedTask = taskRepository.save(task);

        return new TaskResponseDTO(savedTask);

    }

}
