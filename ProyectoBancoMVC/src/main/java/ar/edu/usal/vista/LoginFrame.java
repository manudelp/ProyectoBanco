package ar.edu.usal.vista;

import ar.edu.usal.controlador.LoginController;
import ar.edu.usal.modelo.entidades.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private final JTextField campoCuit;
    private final JButton botonIngresar;
    private final JButton botonRegistrar;
    private final LoginController loginController;

    public LoginFrame() {
        super("Ingreso al sistema");
        this.loginController = new LoginController();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel etiquetaTitulo = new JLabel("Bienvenido al sistema bancario", JLabel.CENTER);
        etiquetaTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelPrincipal.add(etiquetaTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 10, 10));
        JLabel etiqueta = new JLabel("Ingrese su CUIT:");
        campoCuit = new JTextField();
        panelCentro.add(etiqueta);
        panelCentro.add(campoCuit);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botonIngresar = new JButton("Ingresar");
        botonRegistrar = new JButton("Registrarse");
        panelBotones.add(botonIngresar);
        panelBotones.add(botonRegistrar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        botonIngresar.addActionListener(this::procesarIngreso);
        botonRegistrar.addActionListener(e -> new RegisterFrame().setVisible(true));
    }

    private void procesarIngreso(ActionEvent e) {
        String cuit = campoCuit.getText().trim();
        if (cuit.isEmpty()) {
            mostrarError("Debe ingresar un CUIT.");
            return;
        }
        Cliente cliente = loginController.validarCuit(cuit);
        if (cliente == null) {
            mostrarError("CUIT no registrado.");
        } else {
            dispose();
            new MenuPrincipalFrame(cliente).setVisible(true);
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
