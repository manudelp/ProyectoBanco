package ar.edu.usal.modelo.persistencia.dao;

import ar.edu.usal.modelo.entidades.Cliente;

import java.util.List;

public interface ClienteDAO {
    List<Cliente> obtenerTodos();
    void guardar(Cliente cliente);
    void guardarTodos(List<Cliente> clientes);
    void eliminar(String cuit);
    void actualizar(Cliente cliente);
}
