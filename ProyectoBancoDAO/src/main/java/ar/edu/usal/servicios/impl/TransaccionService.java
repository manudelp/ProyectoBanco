package ar.edu.usal.servicios.impl;

import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;
import ar.edu.usal.modelo.persistencia.dao.CuentaDAO;
import ar.edu.usal.modelo.persistencia.dao.TransaccionDAO;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;
import ar.edu.usal.modelo.utilidades.Conversor;
import ar.edu.usal.servicios.ITransaccionService;

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
    public void convertir(Cuenta origen, Cuenta destino, double monto, String cuitCliente) throws Exception {
        origen.extraer(monto);

        Conversor conversor = new Conversor();
        double convertido = conversor.convertir(origen, destino, monto);
        destino.depositar(convertido);

        cuentaDAO.eliminar(origen);
        cuentaDAO.eliminar(destino);
        cuentaDAO.guardar(origen);
        cuentaDAO.guardar(destino);

        Transaccion t = new Transaccion(
                origen.getIdentificador(),
                destino.getIdentificador(),
                monto,
                Transaccion.Tipo.CONVERSION,
                cuitCliente != null ? cuitCliente : "DESCONOCIDO"
        );
        transaccionDAO.guardar(t);
    }

    @Override
    public void transferir(Cuenta origen, Cuenta destino, double monto) throws SaldoInsuficienteException {
        if (origen instanceof Wallet && destino instanceof Wallet) {
            Wallet w1 = (Wallet) origen;
            Wallet w2 = (Wallet) destino;
            if (!w1.getCripto().equals(w2.getCripto())) {
                throw new UnsupportedOperationException("Wallets con diferente cripto tipo no pueden transferir entre sí.");
            }
        }

        boolean esBancaria = (origen instanceof CajaAhorro || origen instanceof CuentaCorriente) &&
                (destino instanceof CajaAhorro || destino instanceof CuentaCorriente);
        if (esBancaria) {
            Moneda monedaOrigen = (origen instanceof CajaAhorro)
                    ? ((CajaAhorro) origen).getMoneda()
                    : ((CuentaCorriente) origen).getMoneda();
            Moneda monedaDestino = (destino instanceof CajaAhorro)
                    ? ((CajaAhorro) destino).getMoneda()
                    : ((CuentaCorriente) destino).getMoneda();
            if (!monedaOrigen.equals(monedaDestino)) {
                throw new UnsupportedOperationException("Las cuentas bancarias deben tener la misma moneda para transferencias.");
            }
        }

        if (origen instanceof CuentaCorriente) {
            CuentaCorriente cc = (CuentaCorriente) origen;
            if (cc.getSaldo() + cc.getDescubierto() < monto) {
                throw new SaldoInsuficienteException("Saldo insuficiente para realizar la transferencia (descubierto excedido).");
            }
        } else {
            if (origen.getSaldo() < monto) {
                throw new SaldoInsuficienteException("Saldo insuficiente para realizar la transferencia.");
            }
        }

        origen.extraer(monto);
        destino.depositar(monto);

        cuentaDAO.eliminar(origen);
        cuentaDAO.eliminar(destino);
        cuentaDAO.guardar(origen);
        cuentaDAO.guardar(destino);

        // Obtener CUIT del origen si es una cuenta bancaria
        String cuit = null;
        if (origen instanceof CajaAhorro) {
            cuit = ((CajaAhorro) origen).getCuit();
        } else if (origen instanceof CuentaCorriente) {
            cuit = ((CuentaCorriente) origen).getCuit();
        }

        // Crear y registrar transacción con nuevo constructor
        Transaccion t = new Transaccion(
                origen.getIdentificador(),
                destino.getIdentificador(),
                monto,
                Transaccion.Tipo.TRANSFERENCIA,
                cuit != null ? cuit : "DESCONOCIDO"
        );
        transaccionDAO.guardar(t);
    }
}
