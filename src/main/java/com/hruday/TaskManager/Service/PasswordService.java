package com.hruday.TaskManager.Service;

import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Password.PasswordResetToken;
import com.hruday.TaskManager.Repository.PasswordRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PasswordService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PasswordService.class);

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


    @Transactional
    public PasswordResetToken createPasswordResetToken(User user) {
        passwordRepository.deleteByUser(user);

        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(java.util.UUID.randomUUID().toString());
        token.setTokenIssueDate(LocalDateTime.now());
        token.setExpiryDate(java.time.LocalDateTime.now().plusMinutes(3));
        return passwordRepository.save(token);
    }

}
