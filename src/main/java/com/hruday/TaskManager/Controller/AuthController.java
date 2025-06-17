package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.DTO.UserDTO.UserRegisterDTO;
import com.hruday.TaskManager.DTO.UserDTO.UserResponseDTO;
import com.hruday.TaskManager.Email.EmailService;
import com.hruday.TaskManager.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> registerUser(@ModelAttribute UserRegisterDTO userRegisterDTO) {
        try {
            UserResponseDTO newUser = userService.createUser(userRegisterDTO);
            logger.info("Created user: {}", userRegisterDTO.getEmpId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (RuntimeException e) {
            logger.error("Could not register new user: {}", userRegisterDTO.getEmpId(), e);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO,
//                                       HttpServletRequest request) {
//        try {
//            User user = userService.authenticateUser(userLoginDTO.getEmpId(), userLoginDTO.getPassword());
//
//            UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(user.getEmpId(), user.getPassword(), user.getAuthorities());
//
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//
//            HttpSession session = request.getSession(true);
//            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
//
//            return ResponseEntity.ok().body(Map.of(
//                    "message", "Login successful",
//                    "redirect", "/"+ user.getEmpId()
//            ));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
//        try {
//            HttpSession session = request.getSession(false);
//            if(session!= null) {
//                session.invalidate();
//            }
//
//            SecurityContextHolder.clearContext();
//
//            return ResponseEntity.ok().body(Map.of(
//                    "message", "Logged out successfully",
//                    "redirect", "/login"
//            ));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed: " + e.getMessage());
//        }
//    }


