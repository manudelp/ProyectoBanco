package ar.edu.usal.modelo.utilidades;

import ar.edu.usal.modelo.entidades.CajaAhorro;
import ar.edu.usal.modelo.entidades.Cuenta;
import ar.edu.usal.modelo.entidades.CuentaCorriente;
import ar.edu.usal.modelo.entidades.Wallet;

import java.util.HashMap;
import java.util.Map;

public class Conversor {
    private final Map<String, Double> tasasConversion;

    public Conversor() {
        tasasConversion = new HashMap<>();
        cargarTasas(); // Puede venir de archivo .properties
    }

    private void cargarTasas() {
        // Fiat ↔ Fiat
        tasasConversion.put("ARS-USD", 0.00084);
        tasasConversion.put("USD-ARS", 1187.75);

        // Cripto ↔ Cripto (tasas aproximadas al 10 junio 2025)
        tasasConversion.put("BTC-ETH", 40.81);
        tasasConversion.put("ETH-BTC", 0.0245);

        tasasConversion.put("BTC-USDT", 67200.0);
        tasasConversion.put("USDT-BTC", 1.0 / 67200.0);

        tasasConversion.put("BTC-ADA", 159000.0);
        tasasConversion.put("ADA-BTC", 1.0 / 159000.0);

        tasasConversion.put("BTC-SOL", 1345.0);
        tasasConversion.put("SOL-BTC", 1.0 / 1345.0);

        tasasConversion.put("ETH-USDT", 1647.0);
        tasasConversion.put("USDT-ETH", 1.0 / 1647.0);

        tasasConversion.put("ETH-ADA", 3900.0);
        tasasConversion.put("ADA-ETH", 1.0 / 3900.0);

        tasasConversion.put("ETH-SOL", 33.0);
        tasasConversion.put("SOL-ETH", 1.0 / 33.0);

        tasasConversion.put("USDT-ADA", 2.37);
        tasasConversion.put("ADA-USDT", 1.0 / 2.37);

        tasasConversion.put("USDT-SOL", 0.0205);
        tasasConversion.put("SOL-USDT", 1.0 / 0.0205);

        tasasConversion.put("ADA-SOL", 0.0086);
        tasasConversion.put("SOL-ADA", 1.0 / 0.0086);

        // USDT ↔ ARS (stablecoin)
        tasasConversion.put("USDT-ARS", 1187.75);
        tasasConversion.put("ARS-USDT", 0.00084);
    }

    public double convertir(Cuenta origen, Cuenta destino, double monto) throws Exception {
        String clave = obtenerClave(origen, destino);
        if (!tasasConversion.containsKey(clave)) throw new Exception("Conversión no soportada: " + clave);
        return monto * tasasConversion.get(clave);
    }

    private String obtenerClave(Cuenta origen, Cuenta destino) {
        String origenTipo = tipoCuenta(origen);
        String destinoTipo = tipoCuenta(destino);
        return origenTipo + "-" + destinoTipo;
    }

    private String tipoCuenta(Cuenta c) {
        if (c instanceof CajaAhorro) {
            return ((CajaAhorro) c).getMoneda().name();
        }
        if (c instanceof CuentaCorriente) {
            return ((CuentaCorriente) c).getMoneda().name();
        }
        if (c instanceof Wallet) {
            return ((Wallet) c).getCripto().name();
        }
        return "Desconocido";
    }

}
