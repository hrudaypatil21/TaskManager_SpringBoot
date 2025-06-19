package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.DTO.TaskDTO.TaskResponseDTO;
import com.hruday.TaskManager.DTO.TaskDTO.UpdateTaskDTO;
import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Security.AuthHelper;
import com.hruday.TaskManager.Service.TaskService;
import com.hruday.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class TaskViewController {

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping("/tasks/search")
    public String searchTasks(
            @RequestParam String query,
            Model model,
            Authentication authentication) {

        List<Task> tasks = taskService.searchTasks(query, authentication);
        model.addAttribute("tasks", tasks);

        return "fragments/task-rows :: taskRows";
    }

    @GetMapping("/fragments/task-count")
    public String getTaskCount(Authentication authentication, Model model) {
        return "fragments/task-count :: taskCount";
    }


    @GetMapping("/task/status-count")
    @ResponseBody
    public String getTaskCountByStatus(@RequestParam String status, Authentication authentication) {
        int count = taskService.getTaskCountByStatus(authentication, status);
        return String.valueOf(count); // plain text response for HTMX
    }

    @GetMapping("fragments/task-card")
    public String getTaskCard(Model model, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "fragments/task-card :: taskCard";
        }
        User user = (User) authentication.getPrincipal();
        List<Task> tasks = taskService.getTasksByUserId(user);

        model.addAttribute("tasks", tasks);

        return "fragments/task-card :: taskCard";
    }

    @GetMapping("fragments/admin-tasks")
    public String getAdminTask(Model model, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "fragments/admin-tasks :: adminTaskTable";
        }
        User user = (User) authentication.getPrincipal();
        List<Task> tasks = taskService.getAdminTasksByUserId(user);

        model.addAttribute("tasks", tasks);

        return "fragments/admin-tasks :: adminTaskTable";
    }

//    @GetMapping("/fragments/user-sidebar")
//    public String showSidebar(Authentication authentication, Model model) {
//        if(authentication!=null && authentication.getPrincipal() instanceof User) {
//            User user = (User) authentication.getPrincipal();
//            model.addAttribute("empName" , user.getEmpName());
//            model.addAttribute("empId", user.getEmpId());
//        }
//        return "fragments/user-sidebar :: userSidebar";
//    }

//    @GetMapping("/fragments/task-expanded")
//    public String getExpandedTaskCard(Authentication authentication, Model model) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return "fragments/task-card :: taskCard";
//        }
//        User user = (User) authentication.getPrincipal();
//        List<Task> tasks = taskService.getTasksByUserId(user);
//
//        model.addAttribute("tasks", tasks);
//
//        return "fragments/task-expanded :: taskExpandedCard";
//    }

    @GetMapping("/task/view")
    @ResponseBody
    public ResponseEntity<List<Task>> getTasks(Authentication authentication) {

        if(authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of());        }
        User user = (User) authentication.getPrincipal();
        List<Task> tasks = taskService.getTasksByUserId(user);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/by-date")
    public String getTasksByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date,
                                 Authentication authentication,
                                 Model model) {
        if(authentication==null || !authentication.isAuthenticated())
        {
            return "fragments/calendar-task :: taskCalendarFragment";
        }
        User user = (User) authentication.getPrincipal();
        String userId = user.getEmpId();
        List<Task> tasks = taskService.getTasksForUserOnDate(Integer.parseInt(userId), date);
        model.addAttribute("tasks", tasks);
        model.addAttribute("selectedDate", date);
        return "fragments/calendar-task :: taskCalendarFragment";
    }


    @GetMapping("/task/expanded/{id}")
    public String getExpandedTask(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("task", task);
        return "fragments/task-expanded :: expandedTaskCard";
    }

//    @GetMapping("/task/status/{status}")
//    public String getTasksByStatus(@PathVariable("status") String status, Authentication authentication, Model model) {
//        if(authentication == null || !authentication.isAuthenticated()) {
//            return "fragments/task-rows :: taskRows";
//        }
//    }

//    @GetMapping("/update")
//    public String updateTask(@ModelAttribute UpdateTaskDTO updateTaskDTO, Model model) {
//        TaskResponseDTO updatedTask = taskService.
//    }

}
