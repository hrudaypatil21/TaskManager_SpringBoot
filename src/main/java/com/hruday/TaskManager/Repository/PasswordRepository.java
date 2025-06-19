package com.hruday.TaskManager.Repository;

import com.hruday.TaskManager.Entity.User;
import com.hruday.TaskManager.Password.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordResetToken, Long> {

    boolean existsByToken(String token);

    void deleteByUser(User user);

    void deleteByUserId(Integer id);

    Optional<PasswordResetToken> findByUserId(Integer id);

    Optional<PasswordResetToken> findByUser(User user);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM PasswordResetToken t " +
            "WHERE t.token = :token AND t.expiryDate < CURRENT_TIMESTAMP")
    boolean isTokenExpired(@Param("token") String token);

    Optional<PasswordResetToken> findByToken(String token);
}
