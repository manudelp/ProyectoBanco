package ar.edu.usal.test;

import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.modelo.persistencia.dao.ClienteDAO;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.impl.CuentaService;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestCuentas {

    public static void main(String[] args) {
        ICuentaService cuentaService = new CuentaService();
        ClienteDAO clienteDAO = DAOFactory.getClienteDAO();
        List<Cliente> clientes = clienteDAO.leerTodo();

        List<Moneda> monedas = Arrays.asList(Moneda.ARS, Moneda.USD);
        List<Cripto> criptos = Arrays.asList(Cripto.BTC, Cripto.ETH, Cripto.USDT, Cripto.ADA, Cripto.SOL);
        Random random = new Random();

        for (Cliente cliente : clientes) {
            // Caja de Ahorro
            Moneda monedaCA = monedas.get(random.nextInt(monedas.size()));
            String cbuCA = "CA-" + cliente.getCuit() + "-" + monedaCA.name();
            CajaAhorro ca = new CajaAhorro(
                5000 + random.nextDouble() * 15000,
                monedaCA,
                cbuCA,
                cliente.getCuit()
            );
            cuentaService.registrarCuenta(ca);

            // Cuenta Corriente
            Moneda monedaCC = monedas.get(random.nextInt(monedas.size()));
            String cbuCC = "CC-" + cliente.getCuit() + "-" + monedaCC.name();
            CuentaCorriente cc = new CuentaCorriente(
                10000 + random.nextDouble() * 20000,
                monedaCC,
                cbuCC,
                cliente.getCuit(),
                10000
            );
            cuentaService.registrarCuenta(cc);

            // Wallet Cripto
            Cripto cripto = Cripto.values()[random.nextInt(Cripto.values().length)];
            String direccion = "WALLET-" + cripto + "-" + cliente.getCuit();
            Wallet wallet = new Wallet(
                0.5 + random.nextDouble() * 3,
                direccion,
                cripto,
                cliente.getCuit()
            );
            cuentaService.registrarCuenta(wallet);
        }

        System.out.println("Cuentas generadas correctamente para los clientes existentes.");
    }
}
