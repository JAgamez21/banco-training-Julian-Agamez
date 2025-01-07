package com.example.banco_training_Julian_Agamez.service;

import com.example.banco_training_Julian_Agamez.exception.CuentaNoEncontradaException;
import com.example.banco_training_Julian_Agamez.model.CuentaBancaria;
import com.example.banco_training_Julian_Agamez.repository.CuentaBancariaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaBancariaService {

    private final CuentaBancariaRepository cuentaBancariaRepository;

    public CuentaBancariaService(CuentaBancariaRepository cuentaBancariaRepository) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
    }

    public CuentaBancaria consultarSaldo(String numeroCuenta) {
        return cuentaBancariaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con número: " + numeroCuenta));
    }

    public String retirarSaldo(String numeroCuenta, double monto) {
        CuentaBancaria cuenta = consultarSaldo(numeroCuenta);
        return cuenta.retirarSaldo(monto);
    }

    public List<CuentaBancaria> obtenerCuentas() {
        return cuentaBancariaRepository.findAll();
    }
}
