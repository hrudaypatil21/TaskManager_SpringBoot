package com.hruday.TaskManager.DTO;

import com.hruday.TaskManager.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    @NotNull
    private String empId;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private User.Role role = User.Role.USER;

}
