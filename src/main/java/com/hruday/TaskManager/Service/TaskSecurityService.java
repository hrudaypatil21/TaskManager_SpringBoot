package com.hruday.TaskManager.Service;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskSecurityService {

    @Autowired
    private TaskRepository taskRepository;

    public boolean canUpdateTask(String currentEmpId, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        return currentEmpId.equals(task.getAssignedBy().getEmpId()) ||
                currentEmpId.equals(task.getAssignedTo().getEmpId()) ||
                task.getAssignedBy().getRoles().contains(User.Role.ROLE_ADMIN);
    }

    public boolean canDeleteTask(String currentEmpId, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        return currentEmpId.equals(task.getAssignedBy().getEmpId()) ||
                task.getAssignedBy().getRoles().contains(User.Role.ROLE_ADMIN);
    }

}
