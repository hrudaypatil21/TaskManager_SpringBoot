package com.hruday.TaskManager.Service;

import com.hruday.TaskManager.DTO.UserDTO.UserLoginDTO;
import com.hruday.TaskManager.DTO.UserDTO.UserRegisterDTO;
import com.hruday.TaskManager.DTO.UserDTO.UserResponseDTO;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.UserRepository;
//import com.hruday.TaskManager.Security.CustomUserDetailsService;
//import com.hruday.TaskManager.Security.JwtResponse;
//import com.hruday.TaskManager.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


//new user registration
@Transactional
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO createUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        if (userRepository.existsByEmpId(userRegisterDTO.getEmpId())) {
            throw new RuntimeException("User with this ID already exists");
        }

        User newUser = new User();
        newUser.setEmpId(userRegisterDTO.getEmpId());
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.setEmpName(userRegisterDTO.getName());
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        Set<User.Role> roles = userRegisterDTO.getRoles();
        newUser.setRoles(
                roles == null || roles.isEmpty() ?
                        Set.of(User.Role.ROLE_USER) : roles
        );

        User savedUser = userRepository.save(newUser);

        return new UserResponseDTO(savedUser);
    }

    public List<User> searchUsers(String query) {
        if (query == null || query.isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.findByNameAndIdContainingIgnoreCase(query);
    }

    public User getUserByEmpId(String empId) {
        return userRepository.findByEmpId(empId)
                .orElseThrow(() -> new RuntimeException("User not found with this ID"));
    }

}
//    public User authenticateUser(String empId, String password) {
//        User user = userRepository.findByEmpId(empId)
//                .orElseThrow(() -> new RuntimeException("User not found with this ID"));
//
//        if(!passwordEncoder.matches(password, user.getPassword())) {
//            throw new RuntimeException("Invalid password");
//        }
//
//        return user;
//    }

