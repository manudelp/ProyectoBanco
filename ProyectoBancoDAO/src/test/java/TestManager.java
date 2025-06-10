import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;
import ar.edu.usal.modelo.persistencia.dao.*;

import java.util.List;

public class TestManager {
    public static void main(String[] args) {

        // ----- DAO INSTANCES -----
        ClienteDAO clienteDAO = DAOFactory.getClienteDAO();
        CuentaDAO cuentaDAO = DAOFactory.getCuentaDAO();
        TransaccionDAO transaccionDAO = DAOFactory.getTransaccionDAO();

        // ----- CREAR CLIENTE -----
        Cliente cliente = new Cliente("20123456789", "Juan", "Pérez", "1112349876", "mail@mail.com", "Casa");
        clienteDAO.guardar(cliente);

        // ----- CREAR CUENTAS -----
        CajaAhorro ca = new CajaAhorro( 150000, Moneda.PESO,"0001112223334445556667", "20123456789");
        CuentaCorriente cc = new CuentaCorriente( 150000, Moneda.PESO, "0001112223334445556667", "20123456789", 1000);
        Wallet wallet = new Wallet(27, "aHASJdghhjkagsd19w3", Cripto.BTC);

        cuentaDAO.guardar(ca);
        cuentaDAO.guardar(cc);
        cuentaDAO.guardar(wallet);

        // ----- CREAR TRANSACCION -----
        Transaccion t = new Transaccion(ca.getCbu(), cc.getCbu(), 25000);
        transaccionDAO.guardar(t);

        // ----- MOSTRAR CLIENTES -----
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        System.out.println("\nListado de clientes:");
        for (Cliente c : clientes) {
            System.out.println("CUIT: " + c.getCuit() +
                    ", Nombre y Apellido: " + c.getNombre() + " " + c.getApellido() +
                    ", Domicilio: " + c.getDomicilio() +
                    ", Teléfono: " + c.getTelefono() +
                    ", Email: " + c.getEmail()
            );
        }

        // ----- MOSTRAR CUENTAS -----
        List<Cuenta> cuentas = cuentaDAO.leerTodo();
        System.out.println("\nListado de cuentas:");
        for (Cuenta cuenta : cuentas) {
            if (cuenta instanceof CajaAhorro) {
                CajaAhorro c = (CajaAhorro) cuenta;
                System.out.println("Caja de Ahorro - CBU: " + c.getCbu() + ", Saldo: " + c.getSaldo());
            } else if (cuenta instanceof CuentaCorriente) {
                CuentaCorriente c = (CuentaCorriente) cuenta;
                System.out.println("Cuenta Corriente - CBU: " + c.getCbu() + ", Saldo: " + c.getSaldo() + ", Descubierto: " + c.getDescubierto());
            } else if (cuenta instanceof Wallet) {
                Wallet w = (Wallet) cuenta;
                System.out.println("Wallet - Dirección: " + w.getDireccion() + ", Tipo: " + w.getTipo() + ", Saldo: " + w.getSaldo());
            }
        }

        // ----- MOSTRAR TRANSACCIONES -----
        List<Transaccion> transacciones = transaccionDAO.obtenerTodas();
        System.out.println("\nListado de transacciones:");
        for (Transaccion tx : transacciones) {
            System.out.println("Origen: " + tx.getOrigen() +
                    ", Destino: " + tx.getDestino() +
                    ", Monto: " + tx.getMonto() +
                    ", Fecha: " + tx.getFecha());
        }

        System.out.println("\nDatos de prueba guardados y cargados correctamente.");
    }
}
