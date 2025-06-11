package ar.edu.usal.test;

import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.ITransaccionService;
import ar.edu.usal.servicios.impl.CuentaService;
import ar.edu.usal.servicios.impl.TransaccionService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TestTransacciones {

    public static void main(String[] args) {
        ICuentaService cuentaService = new CuentaService();
        ITransaccionService transaccionService = new TransaccionService();
        Random random = new Random();

        List<Transaccion.Tipo> tipos = Arrays.asList(
                Transaccion.Tipo.DEPOSITO,
                Transaccion.Tipo.EXTRACCION,
                Transaccion.Tipo.TRANSFERENCIA,
                Transaccion.Tipo.CONVERSION
        );

        List<Cuenta> cuentas = cuentaService.listarTodas()
                .stream()
                .filter(c -> c instanceof CajaAhorro || c instanceof CuentaCorriente || c instanceof Wallet)
                .collect(Collectors.toList());

        if (cuentas.size() < 2) {
            System.out.println("Se requieren al menos dos cuentas para generar transacciones.");
            return;
        }

        for (int i = 0; i < 50; i++) {
            Cuenta origen = cuentas.get(random.nextInt(cuentas.size()));
            Cuenta destino = cuentas.get(random.nextInt(cuentas.size()));
            while (destino.getIdentificador().equals(origen.getIdentificador())) {
                destino = cuentas.get(random.nextInt(cuentas.size()));
            }

            Transaccion.Tipo tipo = tipos.get(random.nextInt(tipos.size()));
            double monto = Math.round((100 + random.nextDouble() * 100000) * 100.0) / 100.0;
            String moneda;

            if (origen instanceof CajaAhorro) {
                moneda = ((CajaAhorro) origen).getMoneda().name();
            } else if (origen instanceof CuentaCorriente) {
                moneda = ((CuentaCorriente) origen).getMoneda().name();
            } else {
                moneda = ((Wallet) origen).getCripto().name();
            }

            LocalDateTime fecha = LocalDateTime.now()
                    .minusDays(random.nextInt(60))
                    .withHour(random.nextInt(24))
                    .withMinute(random.nextInt(60));

            String cuit = null;
            if (origen instanceof CajaAhorro) {
                cuit = ((CajaAhorro) origen).getCuit();
            } else if (origen instanceof CuentaCorriente) {
                cuit = ((CuentaCorriente) origen).getCuit();
            } else {
                cuit = ((Wallet) origen).getCuit();
            }

            Transaccion t = new Transaccion(
                    origen.getIdentificador(),
                    tipo == Transaccion.Tipo.TRANSFERENCIA ? destino.getIdentificador() : origen.getIdentificador(),
                    monto,
                    moneda,
                    tipo,
                    cuit,
                    fecha
            );

            transaccionService.registrar(t);
        }

        System.out.println("Transacciones de prueba generadas exitosamente.");
    }
}
