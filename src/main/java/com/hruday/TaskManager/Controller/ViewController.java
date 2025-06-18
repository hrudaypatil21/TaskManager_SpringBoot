package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.Email.TaskReminderJob;
import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Password.PasswordResetToken;
import com.hruday.TaskManager.Repository.PasswordRepository;
import com.hruday.TaskManager.Repository.TaskRepository;
import com.hruday.TaskManager.Service.PasswordService;
import com.hruday.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("")
public class ViewController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private TaskReminderJob taskReminderJob;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/get-all-users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "fragments/user-dropdown :: userSelect";
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

    @GetMapping("fragments/task-form")
    public String showTaskForm(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("empName", user.getEmpName());
            model.addAttribute("empId", user.getEmpId());
        }
        return "fragments/task-form :: userTaskForm";
    }

    @GetMapping("fragments/admin-task-form")
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

    @PostMapping("/admin-remind-mail")
    @ResponseBody
    public ResponseEntity<String> sendAdminMail(@RequestParam Long taskId, Authentication authentication) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("cant find task"));

        User user = (User) authentication.getPrincipal();

        String currentEmail = user.getEmail();

        if (!task.getAssignedBy().getEmail().equals(currentEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed to send");        }

        taskReminderJob. sendAdminEmail(task);
        return ResponseEntity.ok("Reminder sent successfully");
    }

    @PostMapping("/password-reset-mail")
    @ResponseBody
    public ResponseEntity<String> sendPasswordResetMail(@RequestParam String empId) {
        User user = userService.getUserByEmpId(empId);
        PasswordResetToken passwordResetToken = passwordService.createPasswordResetToken(user);
        taskReminderJob.sendPasswordResetEmail(passwordResetToken);

        return ResponseEntity.ok("Email sent successfully");
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = passwordRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token.");
            return "error-page";
        }
        model.addAttribute("token", token);
        return "change-password";
    }

    @GetMapping("/reset-password")
    public String showResetForm() {

        return "reset-password"; // Thymeleaf view
    }



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
