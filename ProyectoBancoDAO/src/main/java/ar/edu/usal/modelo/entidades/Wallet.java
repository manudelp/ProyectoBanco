package ar.edu.usal.modelo.entidades;

public class Wallet extends Cuenta {
    private final String direccion;
    private final Cripto cripto;
    private static int direccionCounter = 1;
    private final String cuit;

    public Wallet(double saldo, String direccion, Cripto cripto, String cuit) {
        super(saldo);
        this.cripto = cripto;
        this.direccion = direccion;
        this.cuit = cuit;
    }

    public Wallet(Cripto cripto, String cuit) {
        super(0);
        this.cripto = cripto;
        this.direccion = "WALLET-" + cripto + "-" + direccionCounter++;
        this.cuit = cuit;
    }

    @Override
    public void depositar(double monto) {
        saldo += monto;
    }

    @Override
    public void extraer(double monto) {
        if (saldo >= monto) {
            saldo -= monto;
        } else {
            throw new RuntimeException("Saldo insuficiente en Wallet");
        }
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

    public String getCuit() {
        return cuit;
    }

    @Override
    public String toString() {
        return String.format("[Wallet] %s - Saldo: %.2f %s", direccion, saldo, cripto);
    }

    @Override
    public String getTipo() {
        return cripto.name();
    }
}
