package ar.edu.usal.servicios;

import ar.edu.usal.modelo.entidades.Transaccion;
import java.util.List;

public interface ITransaccionService {
    void registrar(Transaccion t);
    List<Transaccion> listarTodas();
    List<Transaccion> filtrarPorMonto(double minimo);
}
