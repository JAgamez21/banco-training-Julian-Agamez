package com.example.banco_training_Julian_Agamez.controller;

import com.example.banco_training_Julian_Agamez.model.CuentaBancaria;
import com.example.banco_training_Julian_Agamez.service.CuentaBancariaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaBancariaController {

    private final CuentaBancariaService cuentaBancariaService;

    public CuentaBancariaController(CuentaBancariaService cuentaBancariaService) {
        this.cuentaBancariaService = cuentaBancariaService;
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<String> consultarSaldo(@PathVariable String numeroCuenta) {
        CuentaBancaria cuenta = cuentaBancariaService.consultarSaldo(numeroCuenta);
        return ResponseEntity.ok(cuenta.consultarSaldo());
    }

    @PostMapping("/{numeroCuenta}/retiro")
    public ResponseEntity<String> retirarSaldo(@PathVariable String numeroCuenta, @RequestParam double monto) {
        String resultado = cuentaBancariaService.retirarSaldo(numeroCuenta, monto);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping
    public ResponseEntity<List<CuentaBancaria>> obtenerCuentas() {
        return ResponseEntity.ok(cuentaBancariaService.obtenerCuentas());
    }
}
