package ar.edu.usal.modelo.entidades;

import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;

public class CajaAhorro extends Cuenta {
    protected Moneda moneda;
    private String cbu;
    private String cuit;


    public CajaAhorro(double saldo, Moneda moneda, String cbu, String cuit) {
        super(saldo);
        this.moneda = moneda;
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

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }


    @Override
    public void extraer(double monto) throws SaldoInsuficienteException {
        if (saldo >= monto) saldo -= monto;
        else throw new SaldoInsuficienteException("Saldo insuficiente en Caja de Ahorro");
    }

    @Override
    public String getIdentificador() {
        return cbu;
    }

}