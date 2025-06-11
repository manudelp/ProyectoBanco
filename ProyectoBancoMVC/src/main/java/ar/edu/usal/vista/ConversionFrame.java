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
        super("Conversión");
        this.cliente = cliente;
        this.cuentaService = new CuentaService();
        this.transaccionService = new TransaccionService();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 330);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.add(new JLabel("Cliente: " + cliente.getNombre() + " " + cliente.getApellido()));
        info.add(new JLabel("CUIT: " + cliente.getCuit()));
        panelPrincipal.add(info, BorderLayout.NORTH);

        comboOrigen = new JComboBox<>();
        comboDestino = new JComboBox<>();
        campoMonto = new JTextField();
        botonConvertir = new JButton("Convertir");

        cargarCuentas();

        JPanel formulario = new JPanel(new GridLayout(6, 1, 10, 10));
        formulario.add(new JLabel("Cuenta Origen:"));
        formulario.add(comboOrigen);
        formulario.add(new JLabel("Cuenta Destino:"));
        formulario.add(comboDestino);
        formulario.add(new JLabel("Monto:"));
        formulario.add(campoMonto);

        panelPrincipal.add(formulario, BorderLayout.CENTER);

        botonConvertir.setPreferredSize(new Dimension(120, 30));
        botonConvertir.addActionListener(e -> procesarConversion());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(botonConvertir);
        panelPrincipal.add(acciones, BorderLayout.SOUTH);

        add(panelPrincipal);
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
            if (origen.equals(destino)) throw new IllegalArgumentException("No se puede convertir entre la misma cuenta.");
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
