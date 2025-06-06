package com.hruday.TaskManager.DTO;

import com.hruday.TaskManager.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    @NotBlank
    private String empId;
    @Email
    private String email;
    @Size(min = 8)
    private String password;
    @NotNull
    private User.Role role = User.Role.USER;

}
