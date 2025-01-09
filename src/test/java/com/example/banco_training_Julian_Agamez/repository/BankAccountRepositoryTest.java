package com.example.banco_training_Julian_Agamez.repository;

import com.example.banco_training_Julian_Agamez.model.BankAccount;
import com.example.banco_training_Julian_Agamez.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BankAccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private BankAccount account;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Andrea Sierra");
        user.setIdentification("123456");
        user = userRepository.save(user);

        account = new BankAccount();
        account.setAccountNumber("12345");
        account.setBalance(1000.0);
        account.setUser(user);
        account = bankAccountRepository.save(account);
    }

    @Test
    void findByAccountNumber_successful() {
        Optional<BankAccount> retrievedAccount = bankAccountRepository.findByAccountNumber("12345");

        assertTrue(retrievedAccount.isPresent());
        assertEquals("12345", retrievedAccount.get().getAccountNumber());
        assertEquals(1000.0, retrievedAccount.get().getBalance());
        assertEquals("Andrea Sierra", retrievedAccount.get().getUser().getName());
    }

    @Test
    void findByAccountNumber_notFound() {
        Optional<BankAccount> retrievedAccount = bankAccountRepository.findByAccountNumber("99999");

        assertFalse(retrievedAccount.isPresent());
    }
}
