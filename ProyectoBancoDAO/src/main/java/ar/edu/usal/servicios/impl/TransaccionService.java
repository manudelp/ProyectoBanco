package ar.edu.usal.servicios.impl;

import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.modelo.persistencia.dao.CuentaDAO;
import ar.edu.usal.modelo.persistencia.dao.TransaccionDAO;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;
import ar.edu.usal.servicios.ITransaccionService;
import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TransaccionService implements ITransaccionService {

    private final TransaccionDAO transaccionDAO = DAOFactory.getTransaccionDAO();
    private final CuentaDAO cuentaDAO = DAOFactory.getCuentaDAO();

    @Override
    public void registrar(Transaccion t) {
        transaccionDAO.guardar(t);
    }

    @Override
    public List<Transaccion> listarTodas() {
        return transaccionDAO.obtenerTodas();
    }

    @Override
    public List<Transaccion> filtrarPorMonto(double minimo) {
        return transaccionDAO.obtenerTodas().stream()
                .filter(t -> t.getMonto() > minimo)
                .collect(Collectors.toList());
    }

    @Override
    public void transferir(Cuenta origen, Cuenta destino, double monto) throws SaldoInsuficienteException {
        if (origen instanceof Wallet && destino instanceof Wallet) {
            throw new UnsupportedOperationException("Transferencias entre Wallets no permitidas");
        }

        if (origen instanceof CajaAhorro || origen instanceof CuentaCorriente) {
            double saldoDisponible = origen instanceof CuentaCorriente
                    ? ((CuentaCorriente) origen).getSaldo() + ((CuentaCorriente) origen).getDescubierto()
                    : origen.getSaldo();

            if (saldoDisponible < monto) {
                throw new SaldoInsuficienteException("Saldo insuficiente para realizar la transferencia.");
            }

            origen.setSaldo(origen.getSaldo() - monto);
            destino.setSaldo(destino.getSaldo() + monto);

            cuentaDAO.eliminar(origen);
            cuentaDAO.eliminar(destino);
            cuentaDAO.guardar(origen);
            cuentaDAO.guardar(destino);

            Transaccion t = new Transaccion(origen.getIdentificador(), destino.getIdentificador(), monto);
            transaccionDAO.guardar(t);
        } else {
            throw new UnsupportedOperationException("Solo se permiten transferencias desde cuentas bancarias.");
        }
    }
}
