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
        JTextField txtOrigen = new JTextField();
        JTextField txtDestino = new JTextField();
        JTextField txtMonto = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("CBU Origen:"));
        panel.add(txtOrigen);
        panel.add(new JLabel("CBU Destino:"));
        panel.add(txtDestino);
        panel.add(new JLabel("Monto:"));
        panel.add(txtMonto);

        int result = JOptionPane.showConfirmDialog(this, panel, "Nueva Transferencia", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String origen = txtOrigen.getText().trim();
                String destino = txtDestino.getText().trim();
                double monto = Double.parseDouble(txtMonto.getText().trim());
                controller.transferir(origen, destino, monto);
                cargarTransacciones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
}
