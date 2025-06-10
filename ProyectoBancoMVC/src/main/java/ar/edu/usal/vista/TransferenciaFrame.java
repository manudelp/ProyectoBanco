package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.entidades.Cuenta;
import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.ITransaccionService;
import ar.edu.usal.servicios.impl.CuentaService;
import ar.edu.usal.servicios.impl.TransaccionService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TransferenciaFrame extends JFrame {

    private final Cliente cliente;
    private final ICuentaService cuentaService;
    private final ITransaccionService transaccionService;

    private JComboBox<Cuenta> comboOrigen;
    private JComboBox<Cuenta> comboDestino;
    private JTextField campoMonto;
    private JButton botonTransferir;

    public TransferenciaFrame(Cliente cliente) {
        super("Transferencia - Cliente: " + cliente.getCuit());
        this.cliente = cliente;
        this.cuentaService = new CuentaService();
        this.transaccionService = new TransaccionService();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 5, 5));

        comboOrigen = new JComboBox<>();
        comboDestino = new JComboBox<>();
        campoMonto = new JTextField();
        botonTransferir = new JButton("Transferir");

        cargarCuentas();

        add(new JLabel("Cuenta Origen:"));
        add(comboOrigen);
        add(new JLabel("Cuenta Destino:"));
        add(comboDestino);
        add(new JLabel("Monto:"));
        add(campoMonto);
        add(botonTransferir);

        botonTransferir.addActionListener(e -> procesarTransferencia());
    }

    private void cargarCuentas() {
        List<Cuenta> cuentas = cuentaService.listarTodas();
        for (Cuenta c : cuentas) {
            comboDestino.addItem(c);
        }

        List<Cuenta> propias = cuentaService.buscarPorCuit(cliente.getCuit());
        for (Cuenta c : propias) {
            comboOrigen.addItem(c);
        }
    }

    private void procesarTransferencia() {
        Cuenta origen = (Cuenta) comboOrigen.getSelectedItem();
        Cuenta destino = (Cuenta) comboDestino.getSelectedItem();
        String montoTexto = campoMonto.getText().trim();

        try {
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) throw new NumberFormatException();

            if (origen.equals(destino)) {
                throw new IllegalArgumentException("La cuenta origen y destino no pueden ser la misma.");
            }

            transaccionService.transferir(origen, destino, monto);

            JOptionPane.showMessageDialog(this, "Transferencia exitosa.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SaldoInsuficienteException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
