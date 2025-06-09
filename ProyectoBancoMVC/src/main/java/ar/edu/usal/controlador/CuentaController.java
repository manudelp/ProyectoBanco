package ar.edu.usal.controlador;

import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.impl.CuentaService;

import java.util.List;

public class CuentaController {

    private final ICuentaService cuentaService;

    public CuentaController() {
        this.cuentaService = new CuentaService();
    }

    public void crearCajaAhorro(String cuit, Moneda moneda, double saldo) {
        String cbu = generarCbu();
        CajaAhorro cuenta = new CajaAhorro(moneda, saldo, cbu, cuit);
        registrarCuenta(cuenta);
    }

    public void crearCuentaCorriente(String cuit, Moneda moneda, double saldo, double descubierto) {
        String cbu = generarCbu();
        CuentaCorriente cuenta = new CuentaCorriente(moneda, saldo, cbu, cuit, descubierto);
        registrarCuenta(cuenta);
    }

    public void crearWallet(CriptoTipo tipo, double saldo, String direccion) {
        Wallet wallet = new Wallet(tipo.getMoneda(), saldo, direccion, tipo);
        registrarCuenta(wallet);
    }

    private String generarCbu() {
        return "004" + System.currentTimeMillis();
    }

    public void registrarCuenta(Cuenta cuenta) {
        cuentaService.registrarCuenta(cuenta);
    }

    public List<Cuenta> buscarPorCuit(String cuit) {
        return cuentaService.buscarPorCuit(cuit);
    }

    public Cuenta buscarPorCbu(String cbu) {
        return cuentaService.buscarPorCbu(cbu);
    }

    public Cuenta buscarPorDireccion(String direccion) {
        return cuentaService.buscarPorDireccion(direccion);
    }

    public void eliminarCuenta(Cuenta cuenta) {
        cuentaService.eliminarCuenta(cuenta);
    }

    public List<Cuenta> listarTodas() {
        return cuentaService.listarTodas();
    }

}
