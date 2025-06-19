package com.hruday.TaskManager.Email;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Password.PasswordResetToken;
import com.hruday.TaskManager.Repository.TaskRepository;
import com.hruday.TaskManager.Service.PasswordService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TaskReminderJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(TaskReminderJob.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordService passwordService;

    private static final String tokenLink = "http://localhost:8080/token-form";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("TaskReminderJob started at {}", LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime upcoming = LocalDateTime.now().plusHours(12);

        try {
        List<Task> upcomingTasks = taskRepository.findAllByDueDateBetween(now, upcoming);

        for (Task task : upcomingTasks) {
            if (task.getAssignedTo().getEmail() != null) {
                sendReminderEmail(task);
            } else {
                logger.warn("No email found for task assignee: {}", task.getAssignedTo().getEmpId());
            }
//                String to = task.getAssignedTo().getEmail();
//                String subject = "Task Reminder: " + task.getTitle();
//                String body = "Your task \"" + task.getTitle() + "\" is due at " + task.getDueDate();
//                emailService.sendMail(to, subject, body);

            }
        } catch (Exception e) {
            logger.error("Error: " + e);
            throw new JobExecutionException(e);
        }
    }

    private void sendReminderEmail(Task task) {
        try {
            String to = task.getAssignedTo().getEmail();
            String subject = "Task Reminder: " + task.getTitle();

            String body = String.format("""
                <html>
                    <body>
                        <h3>Task Reminder</h3>
                        <p>Your task <strong>%s</strong> is due soon.</p>
                        <p><strong>Due Date: </strong> %s</p>
                        <p><strong>Description: </strong> %s</p>
                        <p><strong>Assigner: </strong> %s</p>
                        <p>Please complete it on time.</p>
                    </body>
                </html>
                """,
                    task.getTitle(),
                    DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a").format(task.getDueDate()),
                    task.getDescription(),
                    task.getAssignedBy().getEmpName()
            );
            emailService.sendMail(to, subject, body);
            logger.info("Reminder sent for task {} to {}", task.getId(), to);
        } catch (Exception e) {
            logger.error("Failed to send reminder for task {}", task.getId(), e);
        }
    }

    public void sendAdminEmail(Task task) {
        try {
            String to = task.getAssignedTo().getEmail();
            String subject = "Task Reminder: " + task.getTitle();
            String from = task.getAssignedBy().getEmail();

            String body = String.format("""
                <html>
                    <body>
                        <h3>Task Reminder</h3>
                        <p>Your task <strong>%s</strong> is due soon.</p>
                        <p><strong>Due Date: </strong> %s</p>
                        <p><strong>Description: </strong> %s</p>
                        <p><strong>Assigner: </strong> %s</p>
                        <p>Please complete it on time.</p>
                    </body>
                </html>
                """,
                    task.getTitle(),
                    DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a").format(task.getDueDate()),
                    task.getDescription(),
                    task.getAssignedBy().getEmpName()
            );
            emailService.sendAdminEmail(from, to, subject, body);
            logger.info("Reminder sent for task {} to {}", task.getId(), to);
        } catch (Exception e) {
            logger.error("Failed to send reminder for task {}", task.getId(), e);
        }
    }

    public void sendPasswordResetEmail(PasswordResetToken passwordResetToken) {
        try {
            String to = passwordResetToken.getUser().getEmail();
            String subject = "Reset Password ";

            String body = String.format("""
    <html>
        <body>
            <h3>Use this token to reset your password:</h3>
            <strong>%s</strong>

            <h2>Go to this link and enter the token:</h2>
            <a href="%s">%s</a>
        </body>
    </html>
    """, passwordResetToken.getToken(), tokenLink, tokenLink);

            emailService.setPasswordResetEmail(to, subject, body);
            logger.info("Password Reset mail sent to {} {}", passwordResetToken.getUser().getEmpName(), to);
        } catch (Exception e) {
            logger.error("Failed to Password Reset mail to {}",  passwordResetToken.getUser().getEmpName(), e);
        }
    }
}
