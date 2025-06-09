package ar.edu.usal.servicios.impl;

import ar.edu.usal.modelo.entidades.Cuenta;
import ar.edu.usal.modelo.persistencia.dao.CuentaDAO;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;
import ar.edu.usal.servicios.ICuentaService;

import java.util.List;

public class CuentaService implements ICuentaService {

    private final CuentaDAO dao = DAOFactory.getCuentaDAO();

    @Override
    public void registrarCuenta(Cuenta cuenta) {
        dao.guardar(cuenta);
    }

    @Override
    public void eliminarCuenta(Cuenta cuenta) {
        dao.eliminar(cuenta);
    }

    @Override
    public void actualizarCuenta(Cuenta cuenta) {
        dao.actualizar(cuenta);
    }

    @Override
    public List<Cuenta> buscarPorCuit(String cuit) {
        return dao.buscarPorCuit(cuit);
    }


    @Override
    public Cuenta buscarPorCbu(String cbu) {
        return dao.buscarPorCbu(cbu);
    }

    @Override
    public Cuenta buscarPorDireccion(String direccion) {
        return dao.buscarPorDireccion(direccion);
    }

    @Override
    public List<Cuenta> listarTodas() {
        return dao.leerTodo();
    }
}