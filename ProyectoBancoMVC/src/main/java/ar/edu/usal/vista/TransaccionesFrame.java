package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.entidades.Transaccion;
import ar.edu.usal.servicios.ITransaccionService;
import ar.edu.usal.servicios.impl.TransaccionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransaccionesFrame extends JFrame {

    private final Cliente cliente;
    private final ITransaccionService transaccionService;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> comboTipo;
    private JTextField campoMontoMin;
    private JComboBox<String> comboMoneda;
    private JTextField campoFechaDesde;
    private JTextField campoFechaHasta;
    private JButton botonFiltrar;

    public TransaccionesFrame(Cliente cliente) {
        super("Historial de Transacciones");
        this.cliente = cliente;
        this.transaccionService = new TransaccionService();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.add(new JLabel("Cliente: " + cliente.getNombre() + " " + cliente.getApellido()));
        info.add(new JLabel("CUIT: " + cliente.getCuit()));
        panelPrincipal.add(info, BorderLayout.NORTH);

        comboTipo = new JComboBox<>(new String[]{"TODOS", "DEPOSITO", "EXTRACCION", "TRANSFERENCIA", "CONVERSION"});
        comboMoneda = new JComboBox<>(new String[]{"TODAS", "ARS", "USD", "BTC", "ETH", "USDT", "ADA", "SOL"});
        campoMontoMin = new JTextField(10);
        campoFechaDesde = new JTextField(10);
        campoFechaHasta = new JTextField(10);
        botonFiltrar = new JButton("Filtrar");

        // Filtros
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Columna izquierda: Tipo, Moneda, Monto
        gbc.gridy = 0;
        gbc.gridx = 0;
        panelFiltros.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panelFiltros.add(comboTipo, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        panelFiltros.add(new JLabel("Moneda/Cripto:"), gbc);
        gbc.gridx = 1;
        panelFiltros.add(comboMoneda, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panelFiltros.add(new JLabel("Monto mínimo:"), gbc);
        gbc.gridx = 1;
        panelFiltros.add(campoMontoMin, gbc);

        // Columna derecha: Fecha desde, hasta, botón
        gbc.gridy = 0;
        gbc.gridx = 2;
        panelFiltros.add(new JLabel("Fecha desde (DD/MM/AAAA):"), gbc);
        gbc.gridx = 3;
        panelFiltros.add(campoFechaDesde, gbc);

        gbc.gridy = 1;
        gbc.gridx = 2;
        panelFiltros.add(new JLabel("Fecha hasta (DD/MM/AAAA):"), gbc);
        gbc.gridx = 3;
        panelFiltros.add(campoFechaHasta, gbc);

        gbc.gridy = 2;
        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panelFiltros.add(botonFiltrar, gbc);

        panelPrincipal.add(panelFiltros, BorderLayout.SOUTH);

        modelo = new DefaultTableModel(new Object[]{"Tipo", "Origen", "Destino", "Monto", "Moneda/Cripto", "Fecha"}, 0);
        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(tabla);
        panelPrincipal.add(scroll, BorderLayout.CENTER);

        botonFiltrar.addActionListener(e -> cargarTransacciones());

        add(panelPrincipal);
        cargarTransacciones();
    }

    private void cargarTransacciones() {
        modelo.setRowCount(0);

        String tipoSeleccionado = (String) comboTipo.getSelectedItem();
        String monedaSeleccionada = (String) comboMoneda.getSelectedItem();
        String montoMinStr = campoMontoMin.getText().trim();
        String fechaDesdeStr = campoFechaDesde.getText().trim();
        String fechaHastaStr = campoFechaHasta.getText().trim();

        double montoMin = 0.0;
        LocalDateTime fechaDesde = null;
        LocalDateTime fechaHasta = null;

        try {
            if (!montoMinStr.isEmpty()) {
                montoMin = Double.parseDouble(montoMinStr);
                if (montoMin < 0) throw new NumberFormatException();
            }

            DateTimeFormatter fechaParser = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if (!fechaDesdeStr.isEmpty()) {
                fechaDesde = LocalDate.parse(fechaDesdeStr, fechaParser).atStartOfDay();
            }

            if (!fechaHastaStr.isEmpty()) {
                fechaHasta = LocalDate.parse(fechaHastaStr, fechaParser).atTime(23, 59, 59);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fechas inválidas. Usá el formato DD/MM/AAAA.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final double montoMinFinal = montoMin;
        final LocalDateTime desde = fechaDesde;
        final LocalDateTime hasta = fechaHasta;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        transaccionService.listarTodas().stream()
                .filter(t -> t.getCuitCliente().equals(cliente.getCuit()))
                .filter(t -> tipoSeleccionado.equals("TODOS") || t.getTipo().toString().equals(tipoSeleccionado))
                .filter(t -> monedaSeleccionada.equals("TODAS") || (t.getMoneda() != null && t.getMoneda().equalsIgnoreCase(monedaSeleccionada)))
                .filter(t -> t.getMonto() >= montoMinFinal)
                .filter(t -> {
                    LocalDateTime fecha = t.getFecha();
                    return (desde == null || !fecha.isBefore(desde)) &&
                            (hasta == null || !fecha.isAfter(hasta));
                })
                .sorted((a, b) -> b.getFecha().compareTo(a.getFecha())) // orden descendente
                .forEach(t -> modelo.addRow(new Object[]{
                        t.getTipo(),
                        t.getOrigen(),
                        t.getDestino(),
                        String.format("%.2f", t.getMonto()),
                        t.getMoneda(),
                        t.getFecha().format(formatter)
                }));
    }
}
