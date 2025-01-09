package com.example.banco_training_Julian_Agamez.service;

import com.example.banco_training_Julian_Agamez.dto.ApiResponse;
import com.example.banco_training_Julian_Agamez.exception.AccountNotFoundException;
import com.example.banco_training_Julian_Agamez.exception.UserNotFoundException;
import com.example.banco_training_Julian_Agamez.model.BankAccount;
import com.example.banco_training_Julian_Agamez.model.User;
import com.example.banco_training_Julian_Agamez.repository.BankAccountRepository;
import com.example.banco_training_Julian_Agamez.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_successful() {
        User user = new User();
        user.setId(1L);
        user.setName("Andrea Sierra");
        user.setIdentification("123456");

        BankAccount newAccount = new BankAccount();
        newAccount.setAccountNumber("12345");
        newAccount.setBalance(1000.0);
        newAccount.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(newAccount);

        ApiResponse<BankAccount> response = bankAccountService.createAccount(newAccount);

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("CUENTA_CREADA", response.getCodigo());
        assertNotNull(response.getData());
        verify(bankAccountRepository).save(newAccount);
    }

    @Test
    void createAccount_userNotFound() {
        BankAccount newAccount = new BankAccount();
        newAccount.setAccountNumber("12345");
        newAccount.setBalance(1000.0);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            bankAccountService.createAccount(newAccount);
        });

        assertEquals("Usuario con ID null no existe.", exception.getMessage());
        verify(bankAccountRepository, never()).save(any(BankAccount.class));
    }

    @Test
    void getAccount_successful() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("12345");
        account.setBalance(500.0);

        when(bankAccountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        ApiResponse<BankAccount> response = bankAccountService.getAccount("12345");

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("OPERACION_EXITOSA", response.getCodigo());
        assertNotNull(response.getData());
        assertEquals("12345", response.getData().getAccountNumber());
    }

    @Test
    void getAccount_notFound() {
        when(bankAccountRepository.findByAccountNumber("12345")).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            bankAccountService.getAccount("12345");
        });

        assertEquals("Cuenta no encontrada con número: 12345", exception.getMessage());
    }
}
