package ar.edu.usal.modelo.entidades;

import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;

import java.io.Serializable;

public abstract class Cuenta implements Serializable {
    protected Moneda moneda;
    protected double saldo;

    public Cuenta(Moneda moneda, double saldo) {
        this.moneda = moneda;
        this.saldo = saldo;
    }

    public abstract void depositar(double monto);
    public abstract void extraer(double monto) throws SaldoInsuficienteException;

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
