package com.hruday.TaskManager.Service;

import com.hruday.TaskManager.DTO.UserDTO.UserLoginDTO;
import com.hruday.TaskManager.DTO.UserDTO.UserRegisterDTO;
import com.hruday.TaskManager.DTO.UserDTO.UserResponseDTO;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Repository.UserRepository;
//import com.hruday.TaskManager.Security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtUtil jwtUtil;

//    @Autowired
//    private CustomUserDetailsService userDetailsService;

    //new user registration
    @Transactional
    public UserResponseDTO createUser(UserRegisterDTO userRegisterDTO) {
        if(userRepository.existsByEmail(userRegisterDTO.getEmail())){
            throw new RuntimeException("User with this email already exists");
        }

        if(userRepository.existsByEmpId(userRegisterDTO.getEmpId())){
            throw new RuntimeException("User with this ID already exists");
        }

        User newUser = new User();
        newUser.setEmpId(userRegisterDTO.getEmpId());
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.setName(userRegisterDTO.getName());
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        Set<User.Role> roles = userRegisterDTO.getRoles();
        newUser.setRoles(
                roles == null || roles.isEmpty() ?
                        Set.of(User.Role.ROLE_USER) : roles
        );
//        newUser.setRole(userRegisterDTO.getRole() != null ?
//         userRegisterDTO.getRole() : User.Role.USER);

        User savedUser = userRepository.save(newUser);

        return new UserResponseDTO(savedUser);
    }

    public User authenticateUser(String empId, String password) {
        User user = userRepository.findByEmpId(empId)
                .orElseThrow(() -> new RuntimeException("User not found with this ID"));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    //basic login with id and password
//    @Transactional
//    public JwtResponse loginUser(UserLoginDTO userLoginDTO) {
//        User user = userRepository.findByEmpId(userLoginDTO.getEmpId())
//                .orElseThrow(() -> new RuntimeException("User not found with this ID"));
//
//
//        if(!verifyPassword(userLoginDTO.getPassword(), user)) {
//            throw new RuntimeException("Invalid password");
//        }
////
////        return new UserResponseDTO(user);
//
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                userLoginDTO.getEmpId(),
//                userLoginDTO.getPassword()
//        ));
//
////        final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDTO.getEmpId());
////        final String token = jwtUtil.generateToken(userDetails);
//
//        final String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
//                user.getEmpId(),
//                user.getPassword(),
//                user.getAuthorities()
//        ));
//
//        return new JwtResponse(token);
//
//    }
//
//    @Transactional
//    public void logoutUser(String token) {
//
//    }
//    //verify entered password with stored password
//    public boolean verifyPassword(String rawPassword, User user) {
//        return passwordEncoder.matches(rawPassword, user.getPassword());
//    }
}
//    public UserResponseDTO logoutUser(String empId) {
//
//
//    }
