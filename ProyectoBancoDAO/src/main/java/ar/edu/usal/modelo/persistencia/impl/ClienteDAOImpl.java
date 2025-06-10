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
            System.out.println("Error al guardar el cliente: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> leerTodo() {
        List<Cliente> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");

                // Comunicar si hay una linea mal estructurada
                if (datos.length != 6) {
                    System.out.println("Línea inválida: " + linea);
                    continue;
                }

                Cliente c = new Cliente(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5]);
                lista.add(c);
            }
        } catch (IOException e) {
            System.out.println("Error al leer los clientes: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return leerTodo();
    }

    @Override
    public void eliminar(String cuit) {
        List<Cliente> clientes = leerTodo();
        clientes.removeIf(c -> c.getCuit().equals(cuit));
        guardarTodos(clientes);
    }

    @Override
    public void actualizar(Cliente cliente) {
        List<Cliente> clientes = leerTodo();
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getCuit().equals(cliente.getCuit())) {
                clientes.set(i, cliente);
                break;
            }
        }
        guardarTodos(clientes);
    }

    @Override
    public void guardarTodos(List<Cliente> clientes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
            for (Cliente cliente : clientes) {
                writer.write(
                        cliente.getCuit() + "," +
                                cliente.getNombre() + "," +
                                cliente.getApellido() + "," +
                                cliente.getTelefono() + "," +
                                cliente.getEmail() + "," +
                                cliente.getDomicilio());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar los clientes: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

}
