package ar.edu.usal.modelo.entidades;

import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;
import ar.edu.usal.modelo.utilidades.Conversor;

import java.io.Serializable;

public abstract class Cuenta implements Serializable {
    protected double saldo;

    public Cuenta(double saldo) {
        this.saldo = saldo;
    }

    public abstract void depositar(double monto);

    public abstract void extraer(double monto) throws SaldoInsuficienteException;

    public void convertirA(Cuenta destino, double monto, Conversor conversor) throws Exception {
        if (this == destino) throw new Exception("No se puede convertir a la misma cuenta.");
        this.extraer(monto);
        double convertido = conversor.convertir(this, destino, monto);
        destino.depositar(convertido);
    }

    public abstract String getIdentificador();

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public abstract String getTipo();
}
