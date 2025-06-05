package com.hruday.TaskManager.Entity;

import com.hruday.TaskManager.Password.PasswordUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "emp_id", nullable = false, unique = true)
    private String empId;

    @Column(name = "emp_email", nullable = false, unique = true)
    private String email;

    @Column(name = "emp_name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER; // Default role is USER

    public enum Role {
        USER,
        ADMIN
    }
}
//    public void setPassword(String password) {
//        this.password = PasswordUtil.hashPassword(password);
//    }
//
//    public boolean verifyPassword(String password) {
//        return password.isEmpty() || PasswordUtil.verifyPassword(password, this.password);
//    }
