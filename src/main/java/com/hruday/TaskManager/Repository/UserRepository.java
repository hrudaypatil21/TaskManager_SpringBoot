package com.hruday.TaskManager.Repository;

import com.hruday.TaskManager.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByEmpId(String empId);
//    Optional<User> existsByEmail(String email);
//    Optional<User> existsByEmpId(String empId);

    boolean existsByEmail(String email);
    boolean existsByEmpId(String empId);

    @Query("SELECT u FROM User u WHERE LOWER(u.empName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.empId) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameAndIdContainingIgnoreCase(@Param("name") String name);
}

