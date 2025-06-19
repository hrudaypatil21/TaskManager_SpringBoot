package com.hruday.TaskManager.DTO.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO {

    @NotNull
    private String empId;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

}
