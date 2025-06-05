package com.hruday.TaskManager.Service;

import com.hruday.TaskManager.DTO.UserRegisterDTO;
import com.hruday.TaskManager.DTO.UserResponseDTO;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserResponseDTO createUser(UserRegisterDTO userRegisterDTO) {
        if(userRepository.existsByEmail(userRegisterDTO.getEmail()).isPresent()){
            throw new RuntimeException("User with this email already exists");
        }

        if(userRepository.existsByEmpId(userRegisterDTO.getEmpId()).isPresent()){
            throw new RuntimeException("User with this ID already exists");
        }

        User newUser = new User();
        newUser.setEmpId(userRegisterDTO.getEmpId());
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.getPassword(userRegisterDTO.getPassword());
        newUser.setRole(userRegisterDTO.getRole() != null ?
         userRegisterDTO.getRole() : User.Role.USER);
    }
}
