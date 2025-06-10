package ar.edu.usal.modelo.persistencia.manager;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.persistencia.dao.ClienteDAO;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoriaClienteManager {

    private static final Map<String, Cliente> clientesPorCuit = new HashMap<>();
    private static boolean cargado = false;

    public static void cargarClientes() {
        if (cargado) return;

        ClienteDAO clienteDAO = DAOFactory.getClienteDAO();
        List<Cliente> clientes = clienteDAO.leerTodo();

        for (Cliente c : clientes) {
            clientesPorCuit.put(c.getCuit(), c);
        }

        cargado = true;
        System.out.println("Clientes cargados en memoria: " + clientesPorCuit.size());
    }

    public static Cliente buscarPorCuit(String cuit) {
        return clientesPorCuit.get(cuit);
    }

    public static boolean existeCliente(String cuit) {
        return clientesPorCuit.containsKey(cuit);
    }

    public static List<Cliente> listarClientes() {
        return List.copyOf(clientesPorCuit.values());
    }
}
