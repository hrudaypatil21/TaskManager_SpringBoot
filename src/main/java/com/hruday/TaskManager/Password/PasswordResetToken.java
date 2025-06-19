package com.hruday.TaskManager.Password;

import com.hruday.TaskManager.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "password_reset_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime tokenIssueDate = LocalDateTime.now();

    private LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}



