package com.mvp.backend.repository;
import com.mvp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByToken(String token);
    Optional<User> findByUsernameAndPassword(String username, String password);
}