package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.entidades.Cuenta;
import ar.edu.usal.modelo.entidades.Transaccion;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.ITransaccionService;
import ar.edu.usal.servicios.impl.CuentaService;
import ar.edu.usal.servicios.impl.TransaccionService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DepositoFrame extends JFrame {

    private final Cliente cliente;
    private final ICuentaService cuentaService;
    private final ITransaccionService transaccionService = new TransaccionService();

    private JComboBox<Cuenta> comboCuentas;
    private JTextField campoMonto;
    private JButton botonDepositar;

    public DepositoFrame(Cliente cliente) {
        super("Depósito - Cliente: " + cliente.getCuit());
        this.cliente = cliente;
        this.cuentaService = new CuentaService();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 5, 5));

        comboCuentas = new JComboBox<>();
        cargarCuentas();

        campoMonto = new JTextField();
        botonDepositar = new JButton("Depositar");

        add(new JLabel("Seleccionar cuenta:"));
        add(comboCuentas);
        add(new JLabel("Monto a depositar:"));
        add(campoMonto);
        add(botonDepositar);

        botonDepositar.addActionListener(e -> procesarDeposito());
    }

    private void cargarCuentas() {
        List<Cuenta> cuentas = cuentaService.buscarPorCuit(cliente.getCuit());
        for (Cuenta c : cuentas) {
            comboCuentas.addItem(c);
        }
    }

    private void procesarDeposito() {
        Cuenta cuenta = (Cuenta) comboCuentas.getSelectedItem();
        String montoTexto = campoMonto.getText().trim();

        try {
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) throw new NumberFormatException();
            cuenta.depositar(monto);
            cuentaService.actualizarCuenta(cuenta);
            // Registrar transacción
            Transaccion transaccion = new Transaccion(
                    cuenta.getIdentificador(),
                    "DEPOSITO",
                    monto,
                    Transaccion.Tipo.DEPOSITO,
                    cliente.getCuit()
            );
            transaccionService.registrar(transaccion);
            JOptionPane.showMessageDialog(this, "Depósito exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al depositar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
