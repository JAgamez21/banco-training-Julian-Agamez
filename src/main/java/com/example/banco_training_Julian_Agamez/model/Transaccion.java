package com.example.banco_training_Julian_Agamez.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_transaccion")
public abstract class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double monto;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private CuentaBancaria cuentaBancaria;

    public Transaccion() {
        this.fecha = new Date();
    }

    public Transaccion(double monto, CuentaBancaria cuentaBancaria) {
        this.monto = monto;
        this.fecha = new Date();
        this.cuentaBancaria = cuentaBancaria;
    }

    public abstract String getTipo();

    // Getters y Setters
    public double getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }
}
