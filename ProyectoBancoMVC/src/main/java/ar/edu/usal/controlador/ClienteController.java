package ar.edu.usal.controlador;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.servicios.IClienteService;
import ar.edu.usal.servicios.impl.ClienteService;

import java.util.List;

public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController() {
        this.clienteService = new ClienteService();
    }

    public List<Cliente> obtenerTodos() {
        return clienteService.obtenerTodos();
    }

    public void registrar(Cliente cliente) {
        clienteService.registrarCliente(cliente);
    }

    public void eliminar(String cuit) {
        clienteService.eliminarCliente(cuit);
    }

    public void modificar(Cliente cliente) {
        clienteService.modificarCliente(cliente);
    }

    public Cliente buscarPorCuit(String cuit) {
        return clienteService.buscarPorCuit(cuit);
    }
}
