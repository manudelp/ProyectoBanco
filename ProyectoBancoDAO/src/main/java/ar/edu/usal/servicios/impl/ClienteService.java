package ar.edu.usal.servicios.impl;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.persistencia.dao.ClienteDAO;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;
import ar.edu.usal.servicios.IClienteService;

public class ClienteService implements IClienteService {

    private final ClienteDAO dao = DAOFactory.getClienteDAO();

    @Override
    public void registrarCliente(Cliente c) {
        dao.guardar(c);
    }

    @Override
    public Cliente buscarPorCuit(String cuit) {
        return dao.leerTodo().stream()
                .filter(c -> c.getCuit().equals(cuit))
                .findFirst()
                .orElse(null);
    }

}
