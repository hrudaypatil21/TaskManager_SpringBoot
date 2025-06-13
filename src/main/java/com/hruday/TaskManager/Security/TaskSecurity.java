package com.hruday.TaskManager.Security;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("taskSecurity")
public class TaskSecurity {

    @Autowired
    private TaskRepository taskRepository;

    public boolean isOwnerOrAdmin(Authentication authentication, Long taskId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        User user = (User) authentication.getPrincipal();
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) return false;

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin ||
                task.getAssignedTo().getEmpId().equals(user.getEmpId()) ||
                task.getAssignedBy().getEmpId().equals(user.getEmpId());
    }



}

