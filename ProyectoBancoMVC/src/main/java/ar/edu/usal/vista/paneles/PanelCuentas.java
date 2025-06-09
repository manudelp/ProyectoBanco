package ar.edu.usal.vista.paneles;

import ar.edu.usal.controlador.CuentaController;
import ar.edu.usal.modelo.entidades.CajaAhorro;
import ar.edu.usal.modelo.entidades.Cuenta;
import ar.edu.usal.modelo.entidades.CuentaCorriente;
import ar.edu.usal.modelo.entidades.Wallet;
import ar.edu.usal.vista.dialogos.FormularioAltaCuenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PanelCuentas extends JPanel {

    private final CuentaController controller;
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final JTextField txtCuit;

    public PanelCuentas() {
        this.controller = new CuentaController();
        this.setLayout(new BorderLayout());

        // Panel superior: CUIT + buscar
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("CUIT:"));
        txtCuit = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        panelBusqueda.add(txtCuit);
        panelBusqueda.add(btnBuscar);

        // Tabla
        String[] columnas = {"Tipo", "CBU/Dirección", "Moneda", "Saldo", "Descubierto"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        // Botones de acción
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar Cuenta");
        JButton btnEliminar = new JButton("Eliminar Cuenta");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);

        // Layout general
        this.add(panelBusqueda, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnBuscar.addActionListener(this::buscarCuentas);
        btnAgregar.addActionListener(e -> new FormularioAltaCuenta(controller, this::refrescar).setVisible(true));
        btnEliminar.addActionListener(this::eliminarCuenta);
    }

    private void buscarCuentas(ActionEvent e) {
        refrescar();
    }

    private void refrescar() {
        modelo.setRowCount(0);
        String cuit = txtCuit.getText().trim();
        if (cuit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese CUIT para buscar.");
            return;
        }



        List<Cuenta> cuentas = controller.buscarPorCuit(cuit);
        for (Cuenta c : cuentas) {
            String identificador = "-";
            String descubierto = "-";

            if (c instanceof CajaAhorro) {
                identificador = ((CajaAhorro) c).getCbu();
            } else if (c instanceof CuentaCorriente) {
                identificador = ((CuentaCorriente) c).getCbu();
                descubierto = String.valueOf(((CuentaCorriente) c).getDescubierto());
            } else if (c instanceof Wallet) {
                identificador = ((Wallet) c).getDireccion();
            }

            modelo.addRow(new Object[]{
                    c.getClass().getSimpleName(),
                    identificador,
                    c.getMoneda(),
                    c.getSaldo(),
                    descubierto
            });
        }
    }


    private void eliminarCuenta(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una cuenta para eliminar.");
            return;
        }

        String tipo = modelo.getValueAt(fila, 0).toString();
        String id = modelo.getValueAt(fila, 1).toString(); // CBU o Dirección
        Cuenta cuenta = null;

        switch (tipo) {
            case "CA":
            case "CC":
                cuenta = controller.buscarPorCbu(id);
                break;
            case "WALLET":
                cuenta = controller.buscarPorDireccion(id);
                break;
        }

        if (cuenta == null) {
            JOptionPane.showMessageDialog(this, "Cuenta no encontrada.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar cuenta: " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.eliminarCuenta(cuenta);
            refrescar();
        }
    }
}
