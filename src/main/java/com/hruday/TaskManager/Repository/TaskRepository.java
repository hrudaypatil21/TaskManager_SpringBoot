package com.hruday.TaskManager.Repository;

import com.hruday.TaskManager.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

//    @Query("SELECT t FROM Task t WHERE t.user.empId = :empId")
//    List<Task> findByUserEmpId(@Param("empId") String empId);

//    List<Task> findByUser_EmpId(String empId);

    List<Task> findByStatus(String status);
    List<Task> findByDueDateBefore(LocalDateTime date);
    List<Task> findByTitleContainingIgnoreCase(String title);
    List<Task> findByDescriptionContainingIgnoreCase(String description);

    List<Task> findByAssignedToEmpId(String empId);
    List<Task> findByAssignedByEmpId(String empId);


}
