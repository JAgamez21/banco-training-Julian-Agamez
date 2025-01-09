package com.example.banco_training_Julian_Agamez.repository;

import com.example.banco_training_Julian_Agamez.model.BankAccount;
import com.example.banco_training_Julian_Agamez.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBankAccount(BankAccount bankAccount);
}
