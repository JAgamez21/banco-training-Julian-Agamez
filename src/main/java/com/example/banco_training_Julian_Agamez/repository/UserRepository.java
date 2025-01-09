package com.example.banco_training_Julian_Agamez.repository;

import com.example.banco_training_Julian_Agamez.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentification(String identification);
    Optional<User> findById(long id);
}
