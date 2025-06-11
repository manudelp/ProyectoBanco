package ar.edu.usal.servicios;

import ar.edu.usal.modelo.entidades.Cliente;

public interface IClienteService {
    void registrarCliente(Cliente c);
    Cliente buscarPorCuit(String cuit);
}
