package ar.edu.usal.vista;

import ar.edu.usal.vista.paneles.PanelClientes;
import ar.edu.usal.vista.paneles.PanelCuentas;
import ar.edu.usal.vista.paneles.PanelTransacciones;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        super("Sistema de Exchange - USAL");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Clientes", new PanelClientes());
        tabbedPane.addTab("Cuentas", new PanelCuentas());
        tabbedPane.addTab("Transacciones", new PanelTransacciones());

        add(tabbedPane);
    }
}
