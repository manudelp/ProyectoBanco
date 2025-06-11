package ar.edu.usal.modelo.entidades;

public class CajaAhorro extends Cuenta {
    private Moneda moneda;
    private String cbu;
    private String cuit;
    private static long cbuCounter = 41749577225264L;

    public CajaAhorro(Moneda moneda, String cuit) {
        super(0.0);
        this.moneda = moneda;
        this.cuit = cuit;
        this.cbu = String.format("%016d", cbuCounter++);
    }

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

    @Override
    public void extraer(double monto) {
        if (saldo >= monto) saldo -= monto;
        else throw new RuntimeException("Saldo insuficiente en Caja de Ahorro");
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

    @Override
    public String toString() {
        return String.format("[CajaAhorro] %s - Saldo: %.2f %s", cbu, saldo, moneda);
    }

    @Override
    public String getTipo() {
        return moneda.name();
    }
}
