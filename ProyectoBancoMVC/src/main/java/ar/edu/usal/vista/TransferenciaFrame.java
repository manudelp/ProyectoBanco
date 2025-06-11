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

    private List<Cuenta> todasLasCuentas;
    private List<Cuenta> cuentasPropias;

    public TransferenciaFrame(Cliente cliente) {
        super("Transferencia");
        this.cliente = cliente;
        this.cuentaService = new CuentaService();
        this.transaccionService = new TransaccionService();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel cliente
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.add(new JLabel("Cliente: " + cliente.getNombre() + " " + cliente.getApellido()));
        info.add(new JLabel("CUIT: " + cliente.getCuit()));
        panelPrincipal.add(info, BorderLayout.NORTH);

        // Cuentas y monto
        comboOrigen = new JComboBox<>();
        comboDestino = new JComboBox<>();
        campoMonto = new JTextField();
        botonTransferir = new JButton("Transferir");

        cargarCuentas();

        comboOrigen.addActionListener(e -> filtrarDestinosPorTipo());

        JPanel formulario = new JPanel(new GridLayout(6, 1, 10, 10));
        formulario.add(new JLabel("Cuenta Origen:"));
        formulario.add(comboOrigen);
        formulario.add(new JLabel("Cuenta Destino:"));
        formulario.add(comboDestino);
        formulario.add(new JLabel("Monto:"));
        formulario.add(campoMonto);

        // Panel central extendido con tip explicativo
        JPanel centroExtendido = new JPanel(new BorderLayout(5, 5));
        centroExtendido.add(formulario, BorderLayout.CENTER);

        JLabel tip = new JLabel("Solo se pueden transferir fondos entre cuentas con la misma moneda.");
        tip.setFont(new Font("SansSerif", Font.ITALIC, 11));
        tip.setForeground(Color.DARK_GRAY);
        centroExtendido.add(tip, BorderLayout.SOUTH);

        panelPrincipal.add(centroExtendido, BorderLayout.CENTER);

        botonTransferir.setPreferredSize(new Dimension(120, 30));
        botonTransferir.addActionListener(e -> procesarTransferencia());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(botonTransferir);
        panelPrincipal.add(acciones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void cargarCuentas() {
        todasLasCuentas = cuentaService.listarTodas();
        cuentasPropias = cuentaService.buscarPorCuit(cliente.getCuit());

        comboOrigen.removeAllItems();
        for (Cuenta c : cuentasPropias) {
            comboOrigen.addItem(c);
        }

        filtrarDestinosPorTipo();
    }

    private void filtrarDestinosPorTipo() {
        Cuenta origen = (Cuenta) comboOrigen.getSelectedItem();
        if (origen == null) return;

        comboDestino.removeAllItems();
        for (Cuenta c : todasLasCuentas) {
            if (!c.getIdentificador().equals(origen.getIdentificador()) &&
                    c.getTipo().equals(origen.getTipo())) {
                comboDestino.addItem(c);
            }
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
