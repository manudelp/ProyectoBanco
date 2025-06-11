package ar.edu.usal.servicios;

import ar.edu.usal.modelo.entidades.Cuenta;
import java.util.List;

public interface ICuentaService {
    void registrarCuenta(Cuenta cuenta);
    void actualizarCuenta(Cuenta cuenta);
    List<Cuenta> buscarPorCuit(String cuit);
    List<Cuenta> listarTodas();
}