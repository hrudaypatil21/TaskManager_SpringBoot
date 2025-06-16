package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Security.AuthHelper;
import com.hruday.TaskManager.Service.TaskService;
import com.hruday.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("")
public class ViewController {

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/admin-dashboard")
    public String showAdminDashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("empName", user.getEmpName());
            model.addAttribute("empId", user.getEmpId());
        }
        return "admin-dashboard";
    }

    @GetMapping("/dashboard/task/new")
    public String showTaskForm(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("empName", user.getEmpName());
            model.addAttribute("empId", user.getEmpId());
        }
        return "fragments/task-form :: userTaskForm";
    }

    @GetMapping("/admin-dashboard/task/new")
    public String showAdminTaskForm(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("empName", user.getEmpName());
            model.addAttribute("empId", user.getEmpId());
        }
        return "fragments/admin-task-form :: adminTaskForm";
    }

//    @GetMapping("/fragments/user/{empId}")
//    public String showTaskCardFragment(@PathVariable String empId, Model model) {
//        List<Task> tasks = taskService.getTasksByUserId(empId);
//        model.addAttribute("tasks", tasks);
//        return "fragments/task-card-list";
//    }

    @GetMapping("/fragments/user-sidebar")
    public String showSidebar(Authentication authentication, Model model) {
        if(authentication!=null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("empName" , user.getEmpName());
            model.addAttribute("empId", user.getEmpId());
        }
        return "fragments/user-sidebar :: userSidebar";
    }

    @GetMapping("/dashboard")
    public String showUserDashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("empName", user.getEmpName());
            model.addAttribute("empId", user.getEmpId());
        }
        return "user-dashboard"; // Return the full template, not just a fragment
    }

//    @GetMapping("/fragments/task-form")
//    public String showTaskForm() {
//
//        return "fragments/user-sidebar :: userSidebar";
//    }



//    @GetMapping("/fragments/task-form")
//    public String getTaskFormFragment(Authentication authentication, Model model) {
//        // Add any necessary model attributes
//        model.addAttribute("isAdmin", authentication != null &&
//                authentication.getAuthorities().stream()
//                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
//        return "fragments/task-form :: taskForm";
//    }

    @GetMapping("/searchuser")
    public String searchUsers(@RequestParam("query") String query, Model model) {
        List<User> users = userService.searchUsers(query);
        model.addAttribute("users", users);
        return "fragments/user-suggestions :: results";
    }

}
