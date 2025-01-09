package com.example.banco_training_Julian_Agamez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "El número de cuenta no puede ser nulo.")
    @Size(min = 5, max = 20, message = "El número de cuenta debe tener entre 5 y 20 caracteres.")
    private String accountNumber;

    @Column(nullable = false)
    @Min(value = 0, message = "El saldo inicial no puede ser negativo.")
    private double balance;

    @Column(nullable = false)
    @NotNull(message = "El titular no puede ser nulo.")
    private String holder;

    @Column(nullable = false)
    @NotNull(message = "El tipo de cuenta no puede ser nulo.")
    private String accountType;

    @NotNull(message = "La fecha de apertura no puede ser nula.")
    @Temporal(TemporalType.DATE)
    private Date openingDate;

    @Column(nullable = false)
    @NotNull(message = "La moneda no puede ser nula.")
    private String currency;

    @Column(nullable = false)
    @Min(value = 0, message = "El límite diario no puede ser negativo.")
    private double dailyLimit;

    @Column(nullable = false)
    @Min(value = 0, message = "El total retirado hoy no puede ser negativo.")
    private double totalWithdrawnToday;

    @Column(nullable = false)
    @NotNull(message = "El estado de la cuenta bloqueada no puede ser nulo.")
    private boolean accountBlocked;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Transaction> transactionHistory = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public double getTotalWithdrawnToday() {
        return totalWithdrawnToday;
    }

    public void setTotalWithdrawnToday(double totalWithdrawnToday) {
        this.totalWithdrawnToday = totalWithdrawnToday;
    }

    public boolean isAccountBlocked() {
        return accountBlocked;
    }

    public void setAccountBlocked(boolean accountBlocked) {
        this.accountBlocked = accountBlocked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public void depositBalance(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser positivo.");
        }
        this.balance += amount;
        addTransaction(new Transaction("Depósito", amount, this));
    }

    public String withdrawBalance(double amount) {
        if (accountBlocked) {
            return "La cuenta está bloqueada. No se puede realizar el retiro.";
        }
        if (amount <= 0) {
            return "El monto a retirar debe ser positivo.";
        }
        if (!canWithdrawToday(amount)) {
            return "Límite diario de retiro excedido.";
        }
        if (balance >= amount) {
            decreaseBalance(amount);
            increaseTotalWithdrawnToday(amount);
            addTransaction(new Transaction("Retiro", amount, this));
            return "Retiro exitoso. Saldo actual: " + balance;
        } else {
            return "Saldo insuficiente.";
        }
    }

    private boolean canWithdrawToday(double amount) {
        return totalWithdrawnToday + amount <= dailyLimit;
    }

    private void decreaseBalance(double amount) {
        balance -= amount;
        if (balance < 0) {
            blockAccount();
        }
    }

    private void increaseTotalWithdrawnToday(double amount) {
        totalWithdrawnToday += amount;
    }

    private void blockAccount() {
        accountBlocked = true;
        System.out.println("La cuenta ha sido bloqueada.");
    }

    private void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
}
