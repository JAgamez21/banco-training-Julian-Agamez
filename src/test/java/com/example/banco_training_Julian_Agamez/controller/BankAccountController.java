package com.example.banco_training_Julian_Agamez.controller;

import com.example.banco_training_Julian_Agamez.dto.ApiResponse;
import com.example.banco_training_Julian_Agamez.model.BankAccount;
import com.example.banco_training_Julian_Agamez.model.Transaction;
import com.example.banco_training_Julian_Agamez.service.BankAccountService;
import com.example.banco_training_Julian_Agamez.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.mock;

@WebMvcTest(controllers = BankAccountController.class)
@Import({BankAccountService.class, UserService.class})
class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final BankAccountService bankAccountService = mock(BankAccountService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllAccounts() throws Exception {
        BankAccount account1 = new BankAccount();
        account1.setAccountNumber("12345");
        account1.setHolder("Andrea Sierra");
        account1.setBalance(1000.0);

        BankAccount account2 = new BankAccount();
        account2.setAccountNumber("67890");
        account2.setHolder("Carlos López");
        account2.setBalance(5000.0);

        List<BankAccount> accounts = Arrays.asList(account1, account2);
        ApiResponse<List<BankAccount>> apiResponse = new ApiResponse<>(200, "OPERACION_EXITOSA", "Cuentas obtenidas con éxito.", accounts);

        Mockito.when(bankAccountService.getAllAccounts()).thenReturn(apiResponse);

        mockMvc.perform(get("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.codigo").value("OPERACION_EXITOSA"))
                .andExpect(jsonPath("$.mensaje").value("Cuentas obtenidas con éxito"))
                .andExpect(jsonPath("$.data").isArray());
    }

    // Test: Crear cuenta
    @Test
    void shouldCreateAccount() throws Exception {
        BankAccount account = new BankAccount();
        account.setAccountNumber("12345");
        account.setHolder("Andrea Sierra");
        account.setBalance(1000.0);

        ApiResponse<BankAccount> apiResponse = new ApiResponse<>(201, "CUENTA_CREADA", "Cuenta creada con éxito.", account);

        Mockito.when(bankAccountService.createAccount(any(BankAccount.class))).thenReturn(apiResponse);

        mockMvc.perform(post("/api/accounts/create_account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.codigo").value("CUENTA_CREADA"))
                .andExpect(jsonPath("$.mensaje").value("Cuenta creada con éxito"))
                .andExpect(jsonPath("$.data.accountNumber").value("12345"))
                .andExpect(jsonPath("$.data.holder").value("Andrea Sierra"))
                .andExpect(jsonPath("$.data.balance").value(1000.0));
    }

    // Test: Actualizar cuenta
    @Test
    void shouldUpdateAccount() throws Exception {
        BankAccount account = new BankAccount();
        account.setAccountNumber("12345");
        account.setHolder("Andrea Sierra");
        account.setBalance(1500.0);

        ApiResponse<BankAccount> apiResponse = new ApiResponse<>(200, "CUENTA_ACTUALIZADA", "Cuenta actualizada con éxito.", account);

        Mockito.when(bankAccountService.updateAccount(eq("12345"), any(BankAccount.class))).thenReturn(apiResponse);

        mockMvc.perform(put("/api/accounts/update_account/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.codigo").value("CUENTA_ACTUALIZADA"))
                .andExpect(jsonPath("$.mensaje").value("Cuenta actualizada con éxito"))
                .andExpect(jsonPath("$.data.balance").value(1500.0));
    }

    // Test: Consultar saldo
    @Test
    void shouldGetBalance() throws Exception {
        ApiResponse<Double> apiResponse = new ApiResponse<>(200, "OPERACION_EXITOSA", "Saldo consultado con éxito.", 1000.0);

        Mockito.when(bankAccountService.getBalance("12345")).thenReturn(apiResponse);

        mockMvc.perform(get("/api/accounts/get_balance/12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.codigo").value("OPERACION_EXITOSA"))
                .andExpect(jsonPath("$.mensaje").value("Saldo consultado con éxito"))
                .andExpect(jsonPath("$.data").value(1000.0));
    }

    // Test: Retirar saldo
    @Test
    void shouldWithdrawBalance() throws Exception {
        ApiResponse<Double> apiResponse = new ApiResponse<>(200, "OPERACION_EXITOSA", "Retiro exitoso.", 800.0);

        Mockito.when(bankAccountService.withdrawBalance(eq("12345"), eq(200.0))).thenReturn(apiResponse);

        mockMvc.perform(post("/api/accounts/withdraw_balance/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 200.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.codigo").value("OPERACION_EXITOSA"))
                .andExpect(jsonPath("$.mensaje").value("Retiro exitoso."))
                .andExpect(jsonPath("$.data").value(800.0));
    }

    // Test: Consultar historial de transacciones
    @Test
    void shouldGetTransactionHistory() throws Exception {
        Transaction transaction1 = new Transaction("Deposito", 500.0, null);
        Transaction transaction2 = new Transaction("Retiro", 200.0, null);

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        ApiResponse<List<Transaction>> apiResponse = new ApiResponse<>(200, "OPERACION_EXITOSA", "Historial de transacciones obtenido con éxito.", transactions);

        Mockito.when(bankAccountService.getTransactionHistory("12345")).thenReturn(apiResponse);

        mockMvc.perform(get("/api/accounts/account_history/12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.codigo").value("OPERACION_EXITOSA"))
                .andExpect(jsonPath("$.mensaje").value("Historial de transacciones obtenido con éxito"))
                .andExpect(jsonPath("$.data").isArray());
    }
}
