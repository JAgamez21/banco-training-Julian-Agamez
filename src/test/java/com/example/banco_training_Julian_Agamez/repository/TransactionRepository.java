package com.example.banco_training_Julian_Agamez.repository;

import com.example.banco_training_Julian_Agamez.model.BankAccount;
import com.example.banco_training_Julian_Agamez.model.Transaction;
import com.example.banco_training_Julian_Agamez.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    private BankAccount bankAccount;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("Andrea Sierra");
        user.setIdentification("123456");
        user = userRepository.save(user);

        bankAccount = new BankAccount();
        bankAccount.setAccountNumber("12345");
        bankAccount.setBalance(1000.0);
        bankAccount.setUser(user);
        bankAccount = bankAccountRepository.save(bankAccount);

        Transaction transaction1 = new Transaction("Deposit", 500.0, bankAccount);
        Transaction transaction2 = new Transaction("Withdrawal", 200.0, bankAccount);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
    }

    @Test
    void findByBankAccount_successful() {
        List<Transaction> transactions = transactionRepository.findByBankAccount(bankAccount);

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertEquals("Deposit", transactions.get(0).getType());
        assertEquals(500.0, transactions.get(0).getAmount());
        assertEquals("Withdrawal", transactions.get(1).getType());
        assertEquals(200.0, transactions.get(1).getAmount());
    }

    @Test
    void findByBankAccount_noTransactions() {
        BankAccount newAccount = new BankAccount();
        newAccount.setAccountNumber("67890");
        newAccount.setBalance(3000.0);
        newAccount.setUser(bankAccount.getUser());
        newAccount = bankAccountRepository.save(newAccount);

        List<Transaction> transactions = transactionRepository.findByBankAccount(newAccount);

        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }
}
