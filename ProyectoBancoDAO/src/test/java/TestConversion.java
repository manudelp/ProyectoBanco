import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.modelo.utilidades.Conversor;

public class TestConversion {
    public static void main(String[] args) {
        try {
            // Crear cuentas con constructores válidos
            CajaAhorro cajaAhorro = new CajaAhorro(100000.0, Moneda.ARS, "1234567890", "20-12345678-9");
            CuentaCorriente cuentaDolar = new CuentaCorriente(0.0, Moneda.USD, "9876543210", "20-12345678-9", 500.0);
            Wallet walletBtc = new Wallet(1.0, "1BTCAddress", Cripto.BTC);
            Wallet walletEth = new Wallet(0.0, "1ETHAddress", Cripto.ETH);

            Conversor conversor = new Conversor();

            // ARS → USD
            System.out.println("Saldo inicial CA ARS: " + cajaAhorro.getSaldo());
            System.out.println("Saldo inicial CC USD: " + cuentaDolar.getSaldo());
            cajaAhorro.convertirA(cuentaDolar, 10000.0, conversor);
            System.out.println("Saldo final CA ARS: " + cajaAhorro.getSaldo());
            System.out.println("Saldo final CC USD: " + cuentaDolar.getSaldo());
            System.out.println("----");

            // BTC → ETH
            System.out.println("Saldo inicial BTC: " + walletBtc.getSaldo());
            System.out.println("Saldo inicial ETH: " + walletEth.getSaldo());
            walletBtc.convertirA(walletEth, 0.5, conversor);
            System.out.println("Saldo final BTC: " + walletBtc.getSaldo());
            System.out.println("Saldo final ETH: " + walletEth.getSaldo());
            System.out.println("----");

            // Conversión inválida
            try {
                walletBtc.convertirA(walletEth, 10.0, conversor);
            } catch (Exception e) {
                System.out.println("Error esperado: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
