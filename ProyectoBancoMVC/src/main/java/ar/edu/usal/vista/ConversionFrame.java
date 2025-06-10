package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.entidades.Cuenta;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;
import ar.edu.usal.modelo.utilidades.Conversor;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.ITransaccionService;
import ar.edu.usal.servicios.impl.CuentaService;
import ar.edu.usal.servicios.impl.TransaccionService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConversionFrame extends JFrame {

    private final Cliente cliente;
    private final ICuentaService cuentaService;
    private final ITransaccionService transaccionService;

    private JComboBox<Cuenta> comboOrigen;
    private JComboBox<Cuenta> comboDestino;
    private JTextField campoMonto;
    private JButton botonConvertir;

    public ConversionFrame(Cliente cliente) {
        super("Conversión - Cliente: " + cliente.getCuit());
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
        botonConvertir = new JButton("Convertir");

        cargarCuentas();

        add(new JLabel("Cuenta Origen:"));
        add(comboOrigen);
        add(new JLabel("Cuenta Destino:"));
        add(comboDestino);
        add(new JLabel("Monto:"));
        add(campoMonto);
        add(botonConvertir);

        botonConvertir.addActionListener(e -> procesarConversion());
    }

    private void cargarCuentas() {
        List<Cuenta> cuentas = cuentaService.buscarPorCuit(cliente.getCuit());
        for (Cuenta c : cuentas) {
            comboOrigen.addItem(c);
            comboDestino.addItem(c);
        }
    }

    private void procesarConversion() {
        Cuenta origen = (Cuenta) comboOrigen.getSelectedItem();
        Cuenta destino = (Cuenta) comboDestino.getSelectedItem();
        String montoTexto = campoMonto.getText().trim();

        try {
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) throw new NumberFormatException();
            if (origen.equals(destino)) {
                throw new IllegalArgumentException("No se puede convertir entre la misma cuenta.");
            }
            // Toda la lógica delegada al servicio
            transaccionService.convertir(origen, destino, monto, cliente.getCuit());
            JOptionPane.showMessageDialog(this, "Conversión exitosa.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en conversión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
