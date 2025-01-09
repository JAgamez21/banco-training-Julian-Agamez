package com.example.banco_training_Julian_Agamez.controller;

import com.example.banco_training_Julian_Agamez.dto.ApiResponse;
import com.example.banco_training_Julian_Agamez.exception.UserNotFoundException;
import com.example.banco_training_Julian_Agamez.model.BankAccount;
import com.example.banco_training_Julian_Agamez.model.Transaction;
import com.example.banco_training_Julian_Agamez.model.User;
import com.example.banco_training_Julian_Agamez.service.BankAccountService;
import com.example.banco_training_Julian_Agamez.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final UserService userService;

    public BankAccountController(BankAccountService bankAccountService, UserService userService) {
        this.bankAccountService = bankAccountService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BankAccount>>> getAccounts() {
        ApiResponse<List<BankAccount>> response = bankAccountService.getAllAccounts();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create_account")
    public ResponseEntity<ApiResponse<BankAccount>> createAccount(@RequestBody Map<String, Object> body) {
        try {
            BankAccount newAccount = new BankAccount();
            newAccount.setAccountNumber((String) body.getOrDefault("accountNumber", ""));
            newAccount.setBalance(Double.parseDouble(body.getOrDefault("balance", 0.0).toString()));
            newAccount.setHolder((String) body.getOrDefault("holder", ""));
            newAccount.setAccountType((String) body.getOrDefault("accountType", ""));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            newAccount.setOpeningDate(dateFormat.parse((String) body.getOrDefault("openingDate", "1970-01-01")));

            newAccount.setCurrency((String) body.getOrDefault("currency", ""));
            newAccount.setDailyLimit(Double.parseDouble(body.getOrDefault("dailyLimit", 0.0).toString()));
            newAccount.setTotalWithdrawnToday(Double.parseDouble(body.getOrDefault("totalWithdrawnToday", 0.0).toString()));

            Long userId = Long.parseLong(body.getOrDefault("userId", 0).toString());
            User user = userService.getUserById(userId).getData();
            newAccount.setUser(user);

            ApiResponse<BankAccount> response = bankAccountService.createAccount(newAccount);
            return ResponseEntity.status(response.getStatus()).body(response);

        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "USUARIO_NO_ENCONTRADO", ex.getMessage(), null)
            );
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "ARGUMENTO_INVALIDO", ex.getMessage(), null)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR_INTERNO", "Error procesando la solicitud: " + ex.getMessage(), null)
            );
        }
    }

    @PostMapping("/update_account/{accountNumber}")
    public ResponseEntity<ApiResponse<BankAccount>> updateAccount(@PathVariable String accountNumber, @RequestBody BankAccount updatedAccount) {
        ApiResponse<BankAccount> response = bankAccountService.updateAccount(accountNumber, updatedAccount);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get_account/{accountNumber}")
    public ResponseEntity<ApiResponse<BankAccount>> getAccount(@PathVariable String accountNumber) {
        ApiResponse<BankAccount> response = bankAccountService.getAccount(accountNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get_balance/{accountNumber}")
    public ResponseEntity<ApiResponse<Double>> getBalance(@PathVariable String accountNumber) {
        ApiResponse<Double> response = bankAccountService.getBalance(accountNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/withdraw_balance/{accountNumber}")
    public ResponseEntity<ApiResponse<Double>> withdrawBalance(@PathVariable String accountNumber, @RequestBody Map<String, Object> body) {
        double amount = Double.parseDouble(body.get("amount").toString());
        ApiResponse<Double> response = bankAccountService.withdrawBalance(accountNumber, amount);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/deposit_balance/{accountNumber}")
    public ResponseEntity<ApiResponse<Double>> depositBalance(@PathVariable String accountNumber, @RequestBody Map<String, Object> body) {
        double amount = Double.parseDouble(body.get("amount").toString());
        ApiResponse<Double> response = bankAccountService.depositBalance(accountNumber, amount);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/account_history/{accountNumber}")
    public ResponseEntity<ApiResponse<List<Transaction>>> getAccountHistory(@PathVariable String accountNumber) {
        ApiResponse<List<Transaction>> response = bankAccountService.getTransactionHistory(accountNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
