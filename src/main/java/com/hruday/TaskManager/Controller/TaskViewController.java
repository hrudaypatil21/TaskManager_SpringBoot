package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Security.AuthHelper;
import com.hruday.TaskManager.Service.TaskService;
import com.hruday.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
