package ar.edu.usal.modelo.persistencia.impl;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.persistencia.dao.ClienteDAO;
import ar.edu.usal.modelo.persistencia.manager.GenericStringManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl extends GenericStringManager<Cliente> implements ClienteDAO {

    public ClienteDAOImpl(String path) {
        super(path);
    }

    @Override
    public void guardar(Cliente cliente) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
            // Guardar datos del Cliente separado por comas
            writer.write(
                    cliente.getCuit() + "," +
                    cliente.getNombre() + "," +
                    cliente.getApellido() + "," +
                    cliente.getTelefono() + "," +
                    cliente.getEmail() + "," +
                    cliente.getDomicilio());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cliente> leerTodo() {
        List<Cliente> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Separar datos del Cliente
                String[] datos = linea.split(",");
                // Instanciar Cliente con sus respectivos datos
                Cliente c = new Cliente(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5]);
                // Agregar el Cliente a la lista
                lista.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return leerTodo();
    }
}
