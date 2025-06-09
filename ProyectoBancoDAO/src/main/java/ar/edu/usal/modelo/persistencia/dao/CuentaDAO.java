package ar.edu.usal.modelo.persistencia.dao;

import ar.edu.usal.modelo.entidades.Cuenta;

import java.util.List;

public interface CuentaDAO {
    void guardar(Cuenta cuenta);
    void eliminar(Cuenta cuenta);
    void actualizar(Cuenta cuenta);
    List<Cuenta> buscarPorCuit(String cuit);
    Cuenta buscarPorCbu(String cbu);
    Cuenta buscarPorDireccion(String direccion);
    List<Cuenta> leerTodo();
}
