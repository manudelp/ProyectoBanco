package ar.edu.usal.modelo.persistencia.dao;

import ar.edu.usal.modelo.entidades.Cuenta;

import java.util.List;

public interface CuentaDAO {
    void guardar(Cuenta cuenta);
    Cuenta buscarPorCbu(String cbu);
    List<Cuenta> leerTodo();
}
