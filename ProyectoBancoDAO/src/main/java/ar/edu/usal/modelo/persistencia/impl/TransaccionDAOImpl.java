package ar.edu.usal.modelo.persistencia.impl;

import ar.edu.usal.modelo.entidades.Transaccion;
import ar.edu.usal.modelo.persistencia.dao.TransaccionDAO;
import ar.edu.usal.modelo.persistencia.manager.GenericByteManager;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDAOImpl extends GenericByteManager<Transaccion> implements TransaccionDAO {

    public TransaccionDAOImpl(String path) {
        super(path);
    }

    @Override
    public void guardar(Transaccion t) {
        super.guardar(t);
    }

    @Override
    public List<Transaccion> leerTodo() {
        List<Transaccion> lista = new ArrayList<>();
        if (!archivo.exists()) return lista;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            while (true) {
                Transaccion t = (Transaccion) in.readObject();
                lista.add(t);
            }
        } catch (EOFException e) {
            System.out.println("Fin del archivo, todas las transacciones leídas.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer las transacciones: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Transaccion> obtenerTodas() {
        return leerTodo();
    }
}