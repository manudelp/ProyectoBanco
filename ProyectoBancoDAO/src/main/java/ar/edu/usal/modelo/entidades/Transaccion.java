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

    public Transaccion(String origen, String destino, double monto, Tipo tipo, String cuitCliente) {
        this.origen = origen;
        this.destino = destino;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
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
}
