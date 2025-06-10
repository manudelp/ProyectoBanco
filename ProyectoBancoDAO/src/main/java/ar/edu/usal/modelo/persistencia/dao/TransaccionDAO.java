package ar.edu.usal.modelo.persistencia.dao;

import ar.edu.usal.modelo.entidades.Transaccion;

import java.time.LocalDate;
import java.util.List;

public interface TransaccionDAO {
    void guardar(Transaccion t);
    List<Transaccion> obtenerTodas();

    // Punto 7: Filtrar transacciones en $ o US$ con monto mayor al mínimo
    List<Transaccion> filtrarPorMonedaYMinimo(String moneda, double minimo);

    // Punto 8: Filtrar transacciones desde una dirección BTC en rango de fechas
    List<Transaccion> filtrarPorDireccionYFechas(String direccionOrigen, LocalDate desde, LocalDate hasta);
}
