package ar.edu.usal.modelo.persistencia.factory;

import ar.edu.usal.modelo.persistencia.dao.*;
import ar.edu.usal.modelo.persistencia.impl.*;
import ar.edu.usal.modelo.utilidades.Config;

public class DAOFactory {
    public static ClienteDAO getClienteDAO() {
        return new ClienteDAOImpl(Config.get("ruta.clientes"));
    }

    public static CuentaDAO getCuentaDAO() {
        return new CuentaDAOImpl(Config.get("ruta.cuentas"));
    }

    public static TransaccionDAO getTransaccionDAO() {
        return new TransaccionDAOImpl(Config.get("ruta.transacciones"));
    }
}
