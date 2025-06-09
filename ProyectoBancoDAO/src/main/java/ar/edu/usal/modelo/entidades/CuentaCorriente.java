package ar.edu.usal.modelo.entidades;

import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;

public class CuentaCorriente extends Cuenta {
    private String cbu;
    private String cuit;
    private double descubierto;


    public CuentaCorriente(Moneda moneda, double saldo, String cbu, String cuit, double descubierto) {
        super(moneda, saldo);
        this.cbu = cbu;
        this.cuit = cuit;
        this.descubierto = descubierto;
    }

    @Override
    public void depositar(double monto) {
        this.saldo += monto;
    }

    @Override
    public void extraer(double monto) throws SaldoInsuficienteException {
        if (saldo + descubierto >= monto) saldo -= monto;
        else throw new SaldoInsuficienteException("Saldo insuficiente en Cuenta Corriente");
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public double getDescubierto() {
        return descubierto;
    }

    public void setDescubierto(double descubierto) {
        this.descubierto = descubierto;
    }

    @Override
    public String getIdentificador() {
        return cbu;
    }

}