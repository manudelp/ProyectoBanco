package ar.edu.usal.vista.paneles;

import ar.edu.usal.controlador.CuentaController;
import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.vista.dialogos.FormularioAltaCuenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PanelCuentas extends JPanel {

    private final CuentaController controller = new CuentaController();
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final JTextField txtCuit;

    public PanelCuentas() {
        setLayout(new BorderLayout());

        String[] columnas = {"Tipo", "CBU/Dirección", "Moneda", "Saldo", "Descubierto"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        txtCuit = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnVerTodas = new JButton("Ver todas");
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("CUIT:"));
        panelSuperior.add(txtCuit);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnVerTodas);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnAgregar);
        panelInferior.add(btnEliminar);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        btnBuscar.addActionListener(this::buscar);
        btnVerTodas.addActionListener(e -> cargar(controller.listarTodas()));
        btnAgregar.addActionListener(e -> new FormularioAltaCuenta(controller, this::refrescar).setVisible(true));
        btnEliminar.addActionListener(this::eliminar);

        cargar(controller.listarTodas());
    }

    private void buscar(ActionEvent e) {
        String cuit = txtCuit.getText().trim();
        if (!cuit.isEmpty()) cargar(controller.buscarPorCuit(cuit));
    }

    private void cargar(List<Cuenta> cuentas) {
        modelo.setRowCount(0);
        for (Cuenta c : cuentas) {
            String id = "-";
            String moneda = "-";
            String descubierto = "-";

            if (c instanceof CajaAhorro) {
                CajaAhorro ca = (CajaAhorro) c;
                id = ca.getCbu();
                moneda = ca.getMoneda().toString();
            } else if (c instanceof CuentaCorriente) {
                CuentaCorriente cc = (CuentaCorriente) c;
                id = cc.getCbu();
                moneda = cc.getMoneda().toString();
                descubierto = String.valueOf(cc.getDescubierto());
            } else if (c instanceof Wallet) {
                Wallet w = (Wallet) c;
                id = w.getDireccion();
                moneda = w.getCripto().toString();
            }

            modelo.addRow(new Object[]{
                    c.getClass().getSimpleName(),
                    id,
                    moneda,
                    c.getSaldo(),
                    descubierto
            });
        }
    }

    private void refrescar() {
        String cuit = txtCuit.getText().trim();
        if (cuit.isEmpty()) {
            cargar(controller.listarTodas());
        } else {
            cargar(controller.buscarPorCuit(cuit));
        }
    }


    private void eliminar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una cuenta.");
            return;
        }

        String tipo = modelo.getValueAt(fila, 0).toString();
        String id = modelo.getValueAt(fila, 1).toString();
        Cuenta cuenta = null;

        if (tipo.equals("CajaAhorro") || tipo.equals("CuentaCorriente")) {
            cuenta = controller.buscarPorCbu(id);
        } else if (tipo.equals("Wallet")) {
            cuenta = controller.buscarPorDireccion(id);
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
