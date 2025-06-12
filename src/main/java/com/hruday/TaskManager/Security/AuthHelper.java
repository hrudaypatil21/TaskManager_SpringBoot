package com.hruday.TaskManager.Security;

import com.hruday.TaskManager.Entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        return null;
    }

    public String getEmail() {
        User user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    public String getEmpName() {
        User user = getCurrentUser();
        return user != null ? user.getEmpName() : null;
    }

    public String getEmpId() {
        User user = getCurrentUser();
        return user != null ? user.getEmpId() : null;
    }
}

