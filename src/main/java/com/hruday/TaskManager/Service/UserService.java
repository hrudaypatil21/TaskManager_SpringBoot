package com.hruday.TaskManager.Service;

import com.hruday.TaskManager.DTO.UserLoginDTO;
import com.hruday.TaskManager.DTO.UserRegisterDTO;
import com.hruday.TaskManager.DTO.UserResponseDTO;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //new user registration
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
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        newUser.setRole(userRegisterDTO.getRole() != null ?
         userRegisterDTO.getRole() : User.Role.USER);

        User saveUser = userRepository.save(newUser);

        return new UserResponseDTO(saveUser);
    }

    //basic login with id and password
    @Transactional
    public UserResponseDTO loginUser(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmpId(userLoginDTO.getEmpId())
                .orElseThrow(() -> new RuntimeException("User not found with this ID"));


        if(!verifyPassword(userLoginDTO.getPassword(), user)) {
            throw new RuntimeException("Invalid password");
        }

        return new UserResponseDTO(user);
    }

    public UserResponseDTO logoutUser(String empId) {


    }


    //verify entered password with stored password
    public boolean verifyPassword(String rawPassword, User user) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

}
