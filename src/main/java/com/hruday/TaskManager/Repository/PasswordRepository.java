package com.hruday.TaskManager.Repository;

import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Password.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    void deleteByUser(User user);

    Optional<PasswordResetToken> findByUser(User user);

}
