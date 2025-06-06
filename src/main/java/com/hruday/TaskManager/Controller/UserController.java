package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.DTO.UserLoginDTO;
import com.hruday.TaskManager.DTO.UserRegisterDTO;
import com.hruday.TaskManager.DTO.UserResponseDTO;
import com.hruday.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        UserResponseDTO response = userService.createUser(userRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        UserResponseDTO response = userService.loginUser(userLoginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestBody String empId) {
        userService.logoutUser(empId);
        return ResponseEntity.status(HttpStatus.OK).body("User logged out successfully");
    }



}
