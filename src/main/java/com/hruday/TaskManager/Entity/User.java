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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "emp_id", nullable = false, unique = true)
    private String empId;

    @Column(name = "emp_email", nullable = false, unique = true)
    private String email;

    @Column(name = "emp_name", nullable = false)
    private String name;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles", nullable = false)
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
