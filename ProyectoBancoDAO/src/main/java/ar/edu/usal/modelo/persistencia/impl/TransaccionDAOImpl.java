package ar.edu.usal.modelo.persistencia.impl;

import ar.edu.usal.modelo.entidades.Transaccion;
import ar.edu.usal.modelo.persistencia.dao.TransaccionDAO;
import ar.edu.usal.modelo.persistencia.manager.GenericByteManager;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<Transaccion> filtrarPorMonedaYMinimo(String moneda, double minimo) {
        return leerTodo().stream()
                .filter(t -> (t.getOrigen().contains(moneda) || t.getDestino().contains(moneda)))
                .filter(t -> t.getMonto() > minimo)
                .sorted((a, b) -> b.getFecha().compareTo(a.getFecha()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaccion> filtrarPorDireccionYFechas(String direccionOrigen, LocalDate desde, LocalDate hasta) {
        return leerTodo().stream()
                .filter(t -> t.getOrigen().equals(direccionOrigen))
                .filter(t -> {
                    LocalDate fecha = t.getFecha().toLocalDate();
                    return (!fecha.isBefore(desde) && !fecha.isAfter(hasta));
                })
                .sorted((a, b) -> a.getFecha().compareTo(b.getFecha()))
                .collect(Collectors.toList());
    }
}