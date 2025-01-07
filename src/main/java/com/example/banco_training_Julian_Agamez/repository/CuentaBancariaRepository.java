package com.example.banco_training_Julian_Agamez.repository;

import com.example.banco_training_Julian_Agamez.model.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Long> {
    Optional<CuentaBancaria> findByNumeroCuenta(String numeroCuenta);
}
