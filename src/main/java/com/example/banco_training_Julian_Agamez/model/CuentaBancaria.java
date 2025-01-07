package com.example.banco_training_Julian_Agamez.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class CuentaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroCuenta;

    @Column(nullable = false)
    private double saldo;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private String tipoCuenta;

    @Temporal(TemporalType.DATE)
    private Date fechaApertura;

    @Column(nullable = false)
    private String moneda;

    @Column(nullable = false)
    private double limiteDiario;

    @Column(nullable = false)
    private double totalRetiradoHoy;

    @Column(nullable = false)
    private boolean cuentaBloqueada;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "cuentaBancaria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaccion> historialTransacciones = new ArrayList<>();

    // Getters y Setters

    public String consultarSaldo() {
        return "Saldo actual: " + saldo;
    }

    public String retirarSaldo(double monto) {
        if (cuentaBloqueada) {
            return "La cuenta está bloqueada. No se puede realizar el retiro.";
        }

        if (saldo >= monto && puedeRetirarHoy(monto)) {
            decrementarSaldo(monto);
            incrementarTotalRetiradoHoy(monto);
            return "Retiro exitoso: " + monto;
        } else {
            return "No se puede realizar el retiro: saldo insuficiente o límite diario excedido.";
        }
    }

    public List<Transaccion> verHistorialDeRetiros() {
        return historialTransacciones.stream()
                .filter(transaccion -> transaccion.getTipo().equals("Retiro"))
                .toList();
    }

    // Métodos auxiliares
    private boolean puedeRetirarHoy(double monto) {
        return totalRetiradoHoy + monto <= limiteDiario;
    }

    private void decrementarSaldo(double monto) {
        saldo -= monto;
        if (saldo < 0) {
            bloquearCuenta();
        }
    }

    private void incrementarTotalRetiradoHoy(double monto) {
        totalRetiradoHoy += monto;
    }

    private void bloquearCuenta() {
        cuentaBloqueada = true;
        System.out.println("La cuenta ha sido bloqueada.");
    }
}
