package ar.edu.usal.modelo.entidades;

import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;

public class Wallet extends Cuenta {
    private String direccion;
    private Cripto cripto;

    public Wallet(double saldo, String direccion, Cripto cripto) {
        super(saldo);
        this.cripto = cripto;
        this.direccion = direccion;

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

    public Cripto getCripto() {
        return cripto;
    }

    public void setCripto(Cripto cripto) {
        this.cripto = cripto;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}