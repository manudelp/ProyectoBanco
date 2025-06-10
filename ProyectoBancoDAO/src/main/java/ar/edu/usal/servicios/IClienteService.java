package ar.edu.usal.servicios;

import ar.edu.usal.modelo.entidades.Cliente;
import java.util.List;

public interface IClienteService {
    void registrarCliente(Cliente c);
    void eliminarCliente(String cuit);
    void modificarCliente(Cliente c);
    Cliente buscarPorCuit(String cuit);
    List<Cliente> obtenerTodos();
}
