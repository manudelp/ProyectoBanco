package ar.edu.usal.modelo.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaccion implements Serializable {
    private final String origen;
    private final String destino;
    private final double monto;
    private final LocalDateTime fecha;

    public Transaccion(String origen, String destino, double monto) {
        this.origen = origen;
        this.destino = destino;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
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
}

