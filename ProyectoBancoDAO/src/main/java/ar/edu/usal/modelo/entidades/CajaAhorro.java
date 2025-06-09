package ar.edu.usal.modelo.entidades;

import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;

public class CajaAhorro extends Cuenta {
    private String cbu;
    private String cuit;


    public CajaAhorro(Moneda moneda, double saldo, String cbu, String cuit) {
        super(moneda, saldo);
        this.cbu = cbu;
        this.cuit = cuit;
    }

    @Override
    public void depositar(double monto) {
        this.saldo += monto;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    @Override
    public void extraer(double monto) throws SaldoInsuficienteException {
        if (saldo >= monto) saldo -= monto;
        else throw new SaldoInsuficienteException("Saldo insuficiente en Caja de Ahorro");
    }
}