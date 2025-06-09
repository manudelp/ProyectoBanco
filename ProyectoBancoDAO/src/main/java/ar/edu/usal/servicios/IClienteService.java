package ar.edu.usal.servicios;

import ar.edu.usal.modelo.entidades.Cliente;
import java.util.List;

public interface IClienteService {
    void registrarCliente(Cliente c);
    List<Cliente> listarClientes();
    Cliente buscarPorCuit(String cuit);
}