package com.hruday.TaskManager.Repository;

import com.hruday.TaskManager.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedTo(String assignedTo);
    List<Task> findByStatus(String status);
    List<Task> findByBeforeDueDate(LocalDate beforeDueDate);
    List<Task> findByTitleContainingIgnoreCase(String title);
    List<Task> findByDescriptionContainingIgnoreCase(String description);

}
