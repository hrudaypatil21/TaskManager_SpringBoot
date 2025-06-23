package com.hruday.TaskManager.Repository;

import com.hruday.TaskManager.Entity.Task;
import com.hruday.TaskManager.Entity.User;
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

    List<Task> findByAssignedToId(int id);

    @Query("SELECT t FROM Task t WHERE t.assignedBy.id = :userId AND t.assignedTo.id <> :userId")
    List<Task> findTasksAssignedByUserToOthers(@Param("userId") int userId);

    List<Task> findAllByDueDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM Task t WHERE " +
            "(LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR STR(t.status) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND t.assignedTo.empId = :empId")
    List<Task> searchTasks(
            @Param("query") String query,
            @Param("empId") String empId);

    @Query("SELECT t FROM Task t WHERE " +
            "(LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR STR(t.status) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND t.assignedBy.empId = :adminEmpId " +
            "AND t.assignedTo.empId <> :adminEmpId")
    List<Task> searchAdminAssignedTasks(
            @Param("query") String query,
            @Param("adminEmpId") String empId);



    List<Task> findByAssignedToEmpId(String empId);

    List<Task> findByAssignedByEmpId(String empId);

    List<Task> findByAssignedToIdAndDueDate(int empId, LocalDateTime date);

    int countByAssignedToAndStatus(User user, Task.Status status);

    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE t.assignedBy = :admin " +
            "AND t.assignedTo <> :admin " +
            "AND t.status = :status")
    int countTasksAssignedByAdmin(
            @Param("admin") User admin,
            @Param("status") Task.Status status);

    List<Task> findByAssignedToAndStatus(User user, Task.Status status);

    List<Task> findByAssignedTo(User user);

    List<Task> findAllByAssignedToOrderByDueDateAsc(User user);
    List<Task> findAllByAssignedToOrderByDueDateDesc(User user);
    List<Task> findAllByAssignedToOrderByAssignedDateAsc(User user);
    List<Task> findAllByAssignedToOrderByAssignedDateDesc(User user);

    @Query("SELECT t FROM Task t WHERE t.assignedBy.id = :userId AND t.assignedTo.id <> :userId ORDER BY t.dueDate ASC")
    List<Task> findTasksAssignedByUserToOthersOrderByDueDateAsc(@Param("userId") int userId);

    @Query("SELECT t FROM Task t WHERE t.assignedBy.id = :userId AND t.assignedTo.id <> :userId ORDER BY t.dueDate DESC")
    List<Task> findTasksAssignedByUserToOthersOrderByDueDateDesc(@Param("userId") int userId);

    @Query("SELECT t FROM Task t WHERE t.assignedBy.id = :userId AND t.assignedTo.id <> :userId ORDER BY t.assignedDate ASC")
    List<Task> findTasksAssignedByUserToOthersOrderByAssignedDateAsc(@Param("userId") int userId);

    @Query("SELECT t FROM Task t WHERE t.assignedBy.id = :userId AND t.assignedTo.id <> :userId ORDER BY t.assignedDate DESC")
    List<Task> findTasksAssignedByUserToOthersOrderByAssignedDateDesc(@Param("userId") int userId);
}
