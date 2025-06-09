package ar.edu.usal.modelo.persistencia.dao;

import ar.edu.usal.modelo.entidades.Transaccion;

import java.util.List;

public interface TransaccionDAO {
    void guardar(Transaccion t);
    List<Transaccion> obtenerTodas();
}
