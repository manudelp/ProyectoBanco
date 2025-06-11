package ar.edu.usal.modelo.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaccion implements Serializable {
    private static final long serialVersionUID = 1778075267400069182L;

    public enum Tipo {
        DEPOSITO, EXTRACCION, TRANSFERENCIA, CONVERSION
    }

    private final String origen;
    private final String destino;
    private final double monto;
    private final LocalDateTime fecha;
    private final Tipo tipo;
    private final String cuitCliente;
    private final String moneda;

    public Transaccion(String origen, String destino, double monto, String moneda, Tipo tipo, String cuitCliente) {
        this.origen = origen;
        this.destino = destino;
        this.monto = monto;
        this.moneda = moneda;
        this.fecha = LocalDateTime.now();
        this.tipo = tipo;
        this.cuitCliente = cuitCliente;
    }

    // Para pruebas
    public Transaccion(String origen, String destino, double monto, String moneda, Tipo tipo, String cuitCliente, LocalDateTime fecha) {
        this.origen = origen;
        this.destino = destino;
        this.monto = monto;
        this.moneda = moneda;
        this.fecha = fecha;
        this.tipo = tipo;
        this.cuitCliente = cuitCliente;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getCuitCliente() {
        return cuitCliente;
    }

    public String getMoneda() {
        return moneda;
    }
}
