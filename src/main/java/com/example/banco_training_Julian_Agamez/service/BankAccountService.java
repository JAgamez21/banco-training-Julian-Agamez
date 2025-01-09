package com.example.banco_training_Julian_Agamez.service;

import com.example.banco_training_Julian_Agamez.dto.ApiResponse;
import com.example.banco_training_Julian_Agamez.exception.AccountNotFoundException;
import com.example.banco_training_Julian_Agamez.exception.UserNotFoundException;
import com.example.banco_training_Julian_Agamez.model.Transaction;
import com.example.banco_training_Julian_Agamez.model.User;
import com.example.banco_training_Julian_Agamez.model.BankAccount;
import com.example.banco_training_Julian_Agamez.repository.BankAccountRepository;
import com.example.banco_training_Julian_Agamez.repository.TransactionRepository;
import com.example.banco_training_Julian_Agamez.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse<BankAccount> getAccount(String accountNumber) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con número: " + accountNumber));
        return new ApiResponse<>(HttpStatus.OK.value(), "OPERACION_EXITOSA", "Cuenta consultada con éxito.", account);
    }

    public ApiResponse<Double> getBalance(String accountNumber) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada con número: " + accountNumber));
        return new ApiResponse<>(HttpStatus.OK.value(), "OPERACION_EXITOSA", "Saldo consultado con éxito.", account.getBalance());
    }

    public ApiResponse<Double> withdrawBalance(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor a 0.");
        }

        BankAccount account = getAccount(accountNumber).getData();

        if (amount > account.getBalance()) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el retiro.");
        }
        if (amount > account.getDailyLimit() - account.getTotalWithdrawnToday()) {
            throw new IllegalArgumentException("El monto excede el límite diario permitido.");
        }

        String result = account.withdrawBalance(amount);
        bankAccountRepository.save(account);

        return new ApiResponse<>(HttpStatus.OK.value(), "OPERACION_EXITOSA", result, account.getBalance());
    }

    public ApiResponse<List<BankAccount>> getAllAccounts() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new IllegalStateException("No hay cuentas registradas.");
        }
        return new ApiResponse<>(HttpStatus.OK.value(), "OPERACION_EXITOSA", "Cuentas obtenidas con éxito.", accounts);
    }

    public ApiResponse<BankAccount> createAccount(BankAccount newAccount) {
        if (newAccount.getBalance() < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }

        User user = userRepository.findById(newAccount.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + newAccount.getUser().getId() + " no existe."));

        newAccount.setUser(user);

        BankAccount createdAccount = bankAccountRepository.save(newAccount);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "CUENTA_CREADA", "Cuenta creada con éxito.", createdAccount);
    }

    public ApiResponse<BankAccount> updateAccount(String accountNumber, BankAccount updatedAccount) {
        BankAccount existingAccount = getAccount(accountNumber).getData();

        if (updatedAccount.getBalance() < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo.");
        }

        existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setHolder(updatedAccount.getHolder());
        existingAccount.setAccountType(updatedAccount.getAccountType());
        existingAccount.setOpeningDate(updatedAccount.getOpeningDate());
        existingAccount.setCurrency(updatedAccount.getCurrency());
        existingAccount.setDailyLimit(updatedAccount.getDailyLimit());
        existingAccount.setTotalWithdrawnToday(updatedAccount.getTotalWithdrawnToday());
        existingAccount.setAccountBlocked(updatedAccount.isAccountBlocked());

        BankAccount updatedAccountResponse = bankAccountRepository.save(existingAccount);
        return new ApiResponse<>(HttpStatus.OK.value(), "CUENTA_ACTUALIZADA", "Cuenta actualizada con éxito.", updatedAccountResponse);
    }

    public ApiResponse<Double> depositBalance(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor a 0.");
        }

        BankAccount account = getAccount(accountNumber).getData();
        account.depositBalance(amount);
        bankAccountRepository.save(account);

        return new ApiResponse<>(HttpStatus.OK.value(), "OPERACION_EXITOSA", "Depósito realizado con éxito. Saldo actual: " + account.getBalance(), account.getBalance());
    }

    public ApiResponse<List<Transaction>> getTransactionHistory(String accountNumber) {
        BankAccount account = getAccount(accountNumber).getData();
        List<Transaction> transactions = transactionRepository.findByBankAccount(account);

        if (transactions.isEmpty()) {
            throw new IllegalStateException("No hay transacciones registradas para la cuenta: " + accountNumber);
        }

        return new ApiResponse<>(HttpStatus.OK.value(), "OPERACION_EXITOSA", "Historial de transacciones obtenido con éxito.", transactions);
    }
}
