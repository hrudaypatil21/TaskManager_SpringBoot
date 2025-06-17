package com.hruday.TaskManager.Email;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Repository.TaskRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskReminderJob implements Job {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime upcoming = LocalDateTime.now().plusHours(12);

        List<Task> upcomingTasks = taskRepository.findAllByDueDateBetween(now, upcoming);

        for (Task task : upcomingTasks) {
            if (task.getAssignedTo().getEmail() != null) {
                String to = task.getAssignedTo().getEmail();
                String subject = "Task Reminder: " + task.getTitle();
                String body = "Your task \"" + task.getTitle() + "\" is due at " + task.getDueDate();
                emailService.sendMail(to, subject, body);
            }
        }
    }
}
