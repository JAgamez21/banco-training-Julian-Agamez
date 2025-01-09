package com.example.banco_training_Julian_Agamez.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<com.example.banco_training_Julian_Agamez.model.BankAccount, Long> {
    Optional<com.example.banco_training_Julian_Agamez.model.BankAccount> findByAccountNumber(String accountNumber);
}
