package ar.edu.usal.vista.paneles;

import ar.edu.usal.controlador.TransaccionController;
import ar.edu.usal.modelo.entidades.Transaccion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelTransacciones extends JPanel {

    private final TransaccionController controller;
    private final JTable tabla;
    private final DefaultTableModel modelo;

    public PanelTransacciones() {
        this.controller = new TransaccionController();
        this.setLayout(new BorderLayout());

        String[] columnas = {"Origen", "Destino", "Monto", "Fecha"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        this.add(scroll, BorderLayout.CENTER);

        JButton btnTransferir = new JButton("Transferir");
        btnTransferir.addActionListener(e -> abrirDialogoTransferencia());
        this.add(btnTransferir, BorderLayout.SOUTH);

        cargarTransacciones();
    }

    private void cargarTransacciones() {
        modelo.setRowCount(0);
        List<Transaccion> lista = controller.obtenerTodas();
        for (Transaccion t : lista) {
            modelo.addRow(new Object[]{
                    t.getOrigen(), t.getDestino(), t.getMonto(), t.getFecha()
            });
        }
    }

    private void abrirDialogoTransferencia() {
        JTextField txtOrigen = new JTextField(15);
        JTextField txtDestino = new JTextField(15);
        JTextField txtMonto = new JTextField(15);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("CBU Origen:"), gbc);
        gbc.gridx = 1;
        panel.add(txtOrigen, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("CBU Destino:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDestino, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Monto:"), gbc);
        gbc.gridx = 1;
        panel.add(txtMonto, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Nueva Transferencia", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String origen = txtOrigen.getText().trim();
            String destino = txtDestino.getText().trim();
            String montoStr = txtMonto.getText().trim();

            if (origen.isEmpty() || destino.isEmpty() || montoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }

            double monto;
            try {
                monto = Double.parseDouble(montoStr);
                if (monto <= 0) {
                    JOptionPane.showMessageDialog(this, "El monto debe ser mayor a cero.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Monto inválido.");
                return;
            }

            try {
                controller.transferir(origen, destino, monto);
                cargarTransacciones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en la transferencia: " + ex.getMessage());
            }
        }
    }
}
