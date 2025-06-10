package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.entidades.Transaccion;
import ar.edu.usal.servicios.ITransaccionService;
import ar.edu.usal.servicios.impl.TransaccionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransaccionesFrame extends JFrame {

    private final Cliente cliente;
    private final ITransaccionService transaccionService;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> comboTipo;
    private JTextField campoMontoMin;
    private JButton botonFiltrar;

    public TransaccionesFrame(Cliente cliente) {
        super("Historial de Transacciones - " + cliente.getCuit());
        this.cliente = cliente;
        this.transaccionService = new TransaccionService();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"Tipo", "Origen", "Destino", "Monto", "Fecha"}, 0);
        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        comboTipo = new JComboBox<>(new String[]{"TODOS", "DEPOSITO", "EXTRACCION", "TRANSFERENCIA", "CONVERSION"});
        campoMontoMin = new JTextField();
        botonFiltrar = new JButton("Filtrar");

        JPanel panelFiltros = new JPanel(new GridLayout(1, 5, 5, 5));
        panelFiltros.add(new JLabel("Tipo:"));
        panelFiltros.add(comboTipo);
        panelFiltros.add(new JLabel("Monto mínimo:"));
        panelFiltros.add(campoMontoMin);
        panelFiltros.add(botonFiltrar);
        add(panelFiltros, BorderLayout.NORTH);

        botonFiltrar.addActionListener(e -> cargarTransacciones());

        cargarTransacciones();
    }

    private void cargarTransacciones() {
        modelo.setRowCount(0);
        String tipoSeleccionado = (String) comboTipo.getSelectedItem();
        String montoMinStr = campoMontoMin.getText().trim();
        double montoMin = 0.0;
        try {
            if (!montoMinStr.isEmpty()) {
                montoMin = Double.parseDouble(montoMinStr);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Monto mínimo inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Transaccion> todas = transaccionService.listarTodas();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final double montoMinFinal = montoMin;
        todas.stream()
                .filter(t -> t.getCuitCliente().equals(cliente.getCuit()))
                .filter(t -> tipoSeleccionado.equals("TODOS") || t.getTipo().toString().equals(tipoSeleccionado))
                .filter(t -> t.getMonto() >= montoMinFinal)
                .forEach(t -> modelo.addRow(new Object[]{
                        t.getTipo(),
                        t.getOrigen(),
                        t.getDestino(),
                        t.getMonto(),
                        t.getFecha().format(formatter)
                }));
    }
}
