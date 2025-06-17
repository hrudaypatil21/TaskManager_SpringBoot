package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.Email.TaskReminderJob;
import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.TaskRepository;
import com.hruday.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskReminderJob taskReminderJob;

//    @PostMapping("/search")
//    public String searchUsers(@RequestParam("assignedToId") String query, Model model) {
//        List<User> users = userService.searchUsers(query);
//        model.addAttribute("users", users);
//        return "fragments/user-suggestions :: results";
//    }

    @GetMapping("/select/{empId}")
    public String selectUser(@PathVariable String empId, Model model) {
        User user = userService.getUserByEmpId(empId);
        model.addAttribute("user", user);
        return "fragments/selected-user :: selected";
    }


}
