package com.hruday.TaskManager.DTO;

import com.hruday.TaskManager.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private String empId;
    private String email;
    private Set<User.Role> roles;

    public UserResponseDTO(User user) {
        this.empId = user.getEmpId();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }

}
