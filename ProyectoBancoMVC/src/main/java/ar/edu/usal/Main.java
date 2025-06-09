package ar.edu.usal;

import ar.edu.usal.vista.paneles.PanelClientes;
import ar.edu.usal.vista.paneles.PanelCuentas;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistema de Exchange - USAL");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab("Clientes", new PanelClientes());
            tabbedPane.addTab("Cuentas", new PanelCuentas());

            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}
