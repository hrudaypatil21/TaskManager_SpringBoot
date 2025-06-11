package com.hruday.TaskManager.Repository;

import com.hruday.TaskManager.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByEmpId(String empId);
//    Optional<User> existsByEmail(String email);
//    Optional<User> existsByEmpId(String empId);


    boolean existsByEmail(String email);
    boolean existsByEmpId(String empId);
}
