package ar.edu.usal.modelo.entidades;

import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;

public class Wallet extends Cuenta {
    private String direccion;
    private CriptoTipo tipo;

    public Wallet(Moneda moneda,  double saldo, String direccion, CriptoTipo tipo) {
        super(moneda, saldo);
        this.direccion = direccion;
        this.tipo = tipo;
    }

    @Override
    public void depositar(double monto) {
        this.saldo += monto;
    }

    @Override
    public void extraer(double monto) throws SaldoInsuficienteException {
        if (saldo >= monto) saldo -= monto;
        else throw new SaldoInsuficienteException("Saldo insuficiente en Wallet");
    }

    @Override
    public String getIdentificador() {
        return direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public CriptoTipo getTipo() {
        return tipo;
    }

    public void setTipo(CriptoTipo tipo) {
        this.tipo = tipo;
    }
}