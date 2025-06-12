package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class ViewController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/admin-dashboard")
    public String showAdminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/dashboard")
    public String showUserDashboard() {
        return "user-dashboard";
    }

    @GetMapping("/dashboard/task/new")
    public String showTaskForm() {
        return "task-form";
    }

    @GetMapping("/admin-dashboard/task/new")
    public String showAdminTaskForm() {
        return "admin-task-form";
    }

    @GetMapping("/fragments/user/{empId}")
    public String showTaskCardFragment(@PathVariable String empId, Model model) {
        List<Task> tasks = taskService.getTasksByUserId(empId);
        model.addAttribute("tasks", tasks);
        return "fragments/task-card-list";
    }

    @GetMapping("/fragments/task-form")
    public String getTaskFormFragment(Authentication authentication, Model model) {
        // Add any necessary model attributes
        model.addAttribute("isAdmin", authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        return "fragments/task-form :: taskForm";
    }
}
