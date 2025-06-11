package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/search")
    public String searchUsers(@RequestParam String query, Model model) {
        List<User> users = userService.searchUsers(query);
        model.addAttribute("users", users);
        return "fragments/user-search-results :: results";
    }

    @GetMapping("/select/{id}")
    public String selectUser(@PathVariable String empId, Model model) {
        User user = userService.getUserByEmpId(empId);
        model.addAttribute("user", user);
        return "fragments/selected-user :: selected";
    }


}
