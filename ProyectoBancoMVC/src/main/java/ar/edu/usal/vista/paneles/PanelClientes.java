package ar.edu.usal.vista.paneles;

import ar.edu.usal.controlador.ClienteController;
import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.vista.dialogos.FormularioAltaCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PanelClientes extends JPanel {

    private final ClienteController controller;
    private final JTable tabla;
    private final DefaultTableModel modelo;

    public PanelClientes() {
        this.controller = new ClienteController();
        this.setLayout(new BorderLayout());

        // Modelo de tabla
        String[] columnas = {"CUIT", "Nombre", "Apellido", "Teléfono", "Email", "Domicilio"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        this.add(scrollPane, BorderLayout.CENTER);

        // Botonera
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        this.add(panelBotones, BorderLayout.SOUTH);

        // Cargar datos
        cargarClientes();

        // Acciones
        btnAgregar.addActionListener(this::accionAgregar);
        btnModificar.addActionListener(this::accionModificar);
        btnEliminar.addActionListener(this::accionEliminar);
    }

    private void cargarClientes() {
        modelo.setRowCount(0);
        List<Cliente> clientes = controller.obtenerTodos();
        for (Cliente c : clientes) {
            modelo.addRow(new Object[]{
                    c.getCuit(), c.getNombre(), c.getApellido(),
                    c.getTelefono(), c.getEmail(), c.getDomicilio()
            });
        }
    }

    private void accionAgregar(ActionEvent e) {
        FormularioAltaCliente dialog = new FormularioAltaCliente(controller, unused -> cargarClientes());
        dialog.setVisible(true);
    }

    private void accionModificar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String cuit = (String) modelo.getValueAt(fila, 0);
            Cliente cliente = controller.buscarPorCuit(cuit);
            FormularioAltaCliente dialog = new FormularioAltaCliente(controller, unused -> cargarClientes(), cliente);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para modificar.");
        }
    }

    private void accionEliminar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String cuit = (String) modelo.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar cliente con CUIT: " + cuit + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.eliminar(cuit);
                cargarClientes();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar.");
        }
    }
}
