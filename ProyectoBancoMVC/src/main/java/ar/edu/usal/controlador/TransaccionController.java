package ar.edu.usal.controlador;

import ar.edu.usal.modelo.entidades.Cuenta;
import ar.edu.usal.modelo.entidades.Transaccion;
import ar.edu.usal.servicios.ITransaccionService;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.impl.TransaccionService;
import ar.edu.usal.servicios.impl.CuentaService;
import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;

import java.util.List;

public class TransaccionController {

    private final ITransaccionService transaccionService;
    private final ICuentaService cuentaService;

    public TransaccionController() {
        this.transaccionService = new TransaccionService();
        this.cuentaService = new CuentaService();
    }

    public void transferir(String origenId, String destinoId, double monto) throws SaldoInsuficienteException {
        Cuenta origen = cuentaService.buscarPorCbu(origenId);
        if (origen == null) {
            origen = cuentaService.buscarPorDireccion(origenId);
        }

        Cuenta destino = cuentaService.buscarPorCbu(destinoId);
        if (destino == null) {
            destino = cuentaService.buscarPorDireccion(destinoId);
        }

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        transaccionService.transferir(origen, destino, monto);

        cuentaService.eliminarCuenta(origen);
        cuentaService.eliminarCuenta(destino);
        cuentaService.registrarCuenta(origen);
        cuentaService.registrarCuenta(destino);
    }


    public List<Transaccion> obtenerTodas() {
        return transaccionService.listarTodas();
    }
}