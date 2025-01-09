package com.example.banco_training_Julian_Agamez.repository;

import com.example.banco_training_Julian_Agamez.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Andrea Sierra");
        user.setIdentification("123456");
        user = userRepository.save(user);
    }

    @Test
    void findByIdentification_successful() {
        Optional<User> retrievedUser = userRepository.findByIdentification("123456");

        assertTrue(retrievedUser.isPresent());
        assertEquals("Andrea Sierra", retrievedUser.get().getName());
        assertEquals("123456", retrievedUser.get().getIdentification());
    }

    @Test
    void findByIdentification_notFound() {
        Optional<User> retrievedUser = userRepository.findByIdentification("999999");

        assertFalse(retrievedUser.isPresent());
    }

    @Test
    void findById_successful() {
        Optional<User> retrievedUser = userRepository.findById(user.getId());

        assertTrue(retrievedUser.isPresent());
        assertEquals("Andrea Sierra", retrievedUser.get().getName());
        assertEquals("123456", retrievedUser.get().getIdentification());
    }

    @Test
    void findById_notFound() {
        Optional<User> retrievedUser = userRepository.findById(999L);

        assertFalse(retrievedUser.isPresent());
    }
}
