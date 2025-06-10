package ar.edu.usal;

import ar.edu.usal.modelo.persistencia.manager.MemoriaClienteManager;
import ar.edu.usal.vista.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cargando clientes...");
        MemoriaClienteManager.cargarClientes();

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
