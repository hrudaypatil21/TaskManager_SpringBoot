package com.hruday.TaskManager.DTO;

import com.hruday.TaskManager.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private String empId;
    private String email;
    private User.Role role;

    public UserResponseDTO(User user) {
        this.empId = user.getEmpId();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

}
