package ar.edu.usal.servicios;

import ar.edu.usal.modelo.entidades.Cuenta;
import java.util.List;

public interface ICuentaService {
    void registrarCuenta(Cuenta cuenta);
    Cuenta buscarPorCbu(String cbu);
    List<Cuenta> listarTodas();
}