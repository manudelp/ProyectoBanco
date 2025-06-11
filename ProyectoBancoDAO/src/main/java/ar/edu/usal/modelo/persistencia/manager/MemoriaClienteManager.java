package ar.edu.usal.modelo.persistencia.manager;

import ar.edu.usal.modelo.entidades.*;
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

        // Asociar cuentas a cada cliente (incluye CajaAhorro, CuentaCorriente y Wallet)
        List<Cuenta> cuentas = DAOFactory.getCuentaDAO().leerTodo();
        for (Cliente c : clientes) {
            for (Cuenta cuenta : cuentas) {
                String cuit = null;
                if (cuenta instanceof CajaAhorro) {
                    cuit = ((CajaAhorro) cuenta).getCuit();
                } else if (cuenta instanceof CuentaCorriente) {
                    cuit = ((CuentaCorriente) cuenta).getCuit();
                } else if (cuenta instanceof Wallet) {
                    cuit = ((Wallet) cuenta).getCuit();
                }
                if (cuit != null && cuit.equals(c.getCuit())) {
                    c.agregarCuenta(cuenta);
                }
            }
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