package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Security.AuthHelper;
import com.hruday.TaskManager.Service.TaskService;
import com.hruday.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class TaskViewController {

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;



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

    @GetMapping("/task/view")
    @ResponseBody
    public ResponseEntity<List<Task>> getTasks(Authentication authentication) {

        if(authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of());        }
        User user = (User) authentication.getPrincipal();
        List<Task> tasks = taskService.getTasksByUserId(user);
        return ResponseEntity.ok(tasks);

    }
}
