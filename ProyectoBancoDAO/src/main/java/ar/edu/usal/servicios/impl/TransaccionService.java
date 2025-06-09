package ar.edu.usal.servicios.impl;

import ar.edu.usal.modelo.entidades.Transaccion;
import ar.edu.usal.modelo.persistencia.dao.TransaccionDAO;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;
import ar.edu.usal.servicios.ITransaccionService;

import java.util.List;
import java.util.stream.Collectors;

public class TransaccionService implements ITransaccionService {

    private final TransaccionDAO dao = DAOFactory.getTransaccionDAO();

    @Override
    public void registrar(Transaccion t) {
        dao.guardar(t);
    }

    @Override
    public List<Transaccion> listarTodas() {
        return dao.obtenerTodas();
    }

    @Override
    public List<Transaccion> filtrarPorMonto(double minimo) {
        return dao.obtenerTodas().stream()
                .filter(t -> t.getMonto() > minimo)
                .collect(Collectors.toList());
    }
}
