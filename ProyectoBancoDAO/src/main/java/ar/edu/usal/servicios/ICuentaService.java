package ar.edu.usal.servicios;

import ar.edu.usal.modelo.entidades.Cuenta;
import java.util.List;

public interface ICuentaService {
    void registrarCuenta(Cuenta cuenta);
    void eliminarCuenta(Cuenta cuenta);
    void actualizarCuenta(Cuenta cuenta);
    List<Cuenta> buscarPorCuit(String cuit);
    Cuenta buscarPorCbu(String cbu);
    Cuenta buscarPorDireccion(String direccion);
    List<Cuenta> listarTodas();
}