package ar.edu.usal.servicios.impl;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.persistencia.dao.ClienteDAO;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;
import ar.edu.usal.servicios.IClienteService;

import java.util.ArrayList;
import java.util.List;

public class ClienteService implements IClienteService {

    private final ClienteDAO dao = DAOFactory.getClienteDAO();

    @Override
    public void registrarCliente(Cliente c) {
        dao.guardar(c);
    }

    @Override
    public void eliminarCliente(String cuit) {
        dao.eliminar(cuit);
    }

    @Override
    public void modificarCliente(Cliente c) {
        dao.actualizar(c);
    }

    @Override
    public Cliente buscarPorCuit(String cuit) {
        return dao.leerTodo().stream()
                .filter(c -> c.getCuit().equals(cuit))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return dao.leerTodo();
    }
}
