package ar.edu.usal.modelo.entidades;

public class CuentaCorriente extends Cuenta {
    private Moneda moneda;
    private String cbu;
    private String cuit;
    private double descubierto;
    private static long cbuCounter = 31749577225264L;

    public CuentaCorriente(Moneda moneda, String cuit, double descubierto) {
        super(0.0);
        this.moneda = moneda;
        this.cuit = cuit;
        this.descubierto = descubierto;
        this.cbu = String.format("%016d", cbuCounter++);
    }

    public CuentaCorriente(double saldo, Moneda moneda, String cbu, String cuit, double descubierto) {
        super(saldo);
        this.moneda = moneda;
        this.cbu = cbu;
        this.cuit = cuit;
        this.descubierto = descubierto;
    }

    @Override
    public void depositar(double monto) {
        this.saldo += monto;
    }

    @Override
    public void extraer(double monto) {
        if (saldo + descubierto >= monto) saldo -= monto;
        else throw new RuntimeException("Saldo insuficiente en Cuenta Corriente");
    }

    @Override
    public String getIdentificador() {
        return cbu;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public String getCbu() {
        return cbu;
    }

    public String getCuit() {
        return cuit;
    }

    public double getDescubierto() {
        return descubierto;
    }

    public void setDescubierto(double descubierto) {
        this.descubierto = descubierto;
    }

    @Override
    public String toString() {
        return String.format("[CuentaCorriente] %s - Saldo: %.2f %s", cbu, saldo, moneda);
    }

    @Override
    public String getTipo() {
        return moneda.name();
    }
}