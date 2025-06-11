package com.hruday.TaskManager.Service;


import com.hruday.TaskManager.DTO.TaskDTO.CreateTaskDTO;
import com.hruday.TaskManager.DTO.TaskDTO.TaskResponseDTO;
import com.hruday.TaskManager.DTO.TaskDTO.UpdateTaskDTO;
import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.TaskRepository;
import com.hruday.TaskManager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  TaskRepository taskRepository;

    @Transactional
    public TaskResponseDTO createTask(CreateTaskDTO createTaskDTO) {
//        User user = userRepository.findByEmpId(createTaskDTO.getAssignedBy().getEmpId())
//                .orElseThrow(() -> new RuntimeException("User not found with this ID"));
//
//        if (user.getRole() == User.Role.ADMIN || Objects.equals(user.getEmpId(), createTaskDTO.getAssignedTo().getEmpId())) {
//
//        }

        User assignedBy = userRepository.findByEmpId(createTaskDTO.getAssignedById())
                .orElseThrow(() -> new RuntimeException("Assigner not found"));

        User assignedTo = userRepository.findByEmpId(createTaskDTO.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));

//        if(createTaskDTO.getAssignedBy().getRole() != User.Role.ADMIN ||
//                createTaskDTO.getAssignedBy().getRole() != createTaskDTO.getAssignedTo().getRole()
//        ) {
//            throw new RuntimeException("Only Admin can assign tasks to other users or users can assign tasks to themselves");
//        }

        boolean isAdmin = assignedBy.getRoles().contains(User.Role.ROLE_ADMIN);
        boolean selfAssign = assignedBy.getRoles().equals(assignedTo.getRoles());

        if(isAdmin) {
            System.out.println("Admin assigning");
        } else if (selfAssign) {
            System.out.println("Self assigning");
        } else {
            throw new RuntimeException("Only admins can assign tasks to others");
        }

//        if (!assignedBy.getRoles().contains(User.Role.ROLE_ADMIN)
//        && !assignedBy.getRoles().equals(assignedTo.getRoles())) {
//            throw new RuntimeException("Only admin can assign task to others and users can assign tasks to themselves.");
//        }

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

        if (updateTaskDTO.getAssignedToId() != null) {
            User assignedTo = userRepository.findByEmpId(updateTaskDTO.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + updateTaskDTO.getAssignedToId()));
            task.setAssignedTo(assignedTo);

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

    public List<Task> getTasksByUserId(String empId) {
        List<Task> tasks = taskRepository.findByAssignedToEmpId(empId);
        if (tasks.isEmpty()) {
            throw new RuntimeException("No tasks found for user with ID " + empId);
        }
        return tasks;
    }

    public List<Task> getTaskById(String empId) {
        List<Task> tasks = taskRepository.findByAssignedToEmpId(empId);
        if(tasks.isEmpty()) {
            throw new RuntimeException("No tasks found");
        }

        return tasks;
    }
}
