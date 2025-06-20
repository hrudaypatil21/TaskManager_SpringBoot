package com.hruday.TaskManager.Service;

import com.hruday.TaskManager.DTO.UserDTO.UpdatePasswordDTO;
import com.hruday.TaskManager.DTO.UserDTO.UserResponseDTO;
import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Password.PasswordResetToken;
import com.hruday.TaskManager.Repository.PasswordRepository;
import com.hruday.TaskManager.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PasswordService.class);

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Transactional
    public PasswordResetToken createPasswordResetToken(User user) {
        Integer userId = user.getId();
        logger.info("Creating token for user ID: {}", userId);

        Optional<PasswordResetToken> existing = passwordRepository.findByUserId(userId);
        if (existing.isPresent()) {
            logger.info("Existing token found for user {}. Deleting it.", userId);
            passwordRepository.deleteByUserId(userId);
            entityManager.flush();
        }

        String generatedToken = UUID.randomUUID().toString();
        logger.info("Generated new token: {}", generatedToken);

        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(generatedToken);
        token.setTokenIssueDate(LocalDateTime.now());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(20));

        PasswordResetToken savedToken = passwordRepository.save(token);
        logger.info("Saved token with ID: {}", savedToken.getId());

        return savedToken;
    }

    @Transactional
    public UserResponseDTO changePassword(UpdatePasswordDTO updatePasswordDTO) {
        String tokenValue = updatePasswordDTO.getToken().trim();

        PasswordResetToken token = passwordRepository.findByToken(tokenValue)
                .orElseThrow(() -> new RuntimeException("Wrong token."));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.error("Token expired: {}", tokenValue);
            throw new RuntimeException("Token expired");
        }

        if (!updatePasswordDTO.getPassword().equals(updatePasswordDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }


        User updatePassUser = token.getUser();

        updatePassUser.setPassword(passwordEncoder.encode(updatePasswordDTO.getPassword()));

        User savedPassUser = userRepository.save(updatePassUser);

        passwordRepository.delete(token);

        return new UserResponseDTO(savedPassUser);
    }

//    public void checkToken(UpdatePasswordDTO updatePasswordDTO) {
//        passwordRepository.isTokenExpired(updatePasswordDTO.getToken());
//    }

}
