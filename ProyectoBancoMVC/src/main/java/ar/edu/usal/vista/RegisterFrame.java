package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.servicios.IClienteService;
import ar.edu.usal.servicios.impl.ClienteService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final JTextField campoCuit = new JTextField();
    private final JTextField campoNombre = new JTextField();
    private final JTextField campoApellido = new JTextField();
    private final JTextField campoTelefono = new JTextField();
    private final JTextField campoEmail = new JTextField();
    private final JTextField campoDomicilio = new JTextField();

    private final IClienteService clienteService = new ClienteService();

    public RegisterFrame() {
        super("Registro de Cliente");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("CUIT:")); add(campoCuit);
        add(new JLabel("Nombre:")); add(campoNombre);
        add(new JLabel("Apellido:")); add(campoApellido);
        add(new JLabel("Teléfono:")); add(campoTelefono);
        add(new JLabel("Email:")); add(campoEmail);
        add(new JLabel("Domicilio:")); add(campoDomicilio);
        add(new JLabel());

        JButton botonRegistrar = new JButton("Registrar");
        add(botonRegistrar);

        botonRegistrar.addActionListener(e -> registrarCliente());
    }

    private void registrarCliente() {
        String cuit = campoCuit.getText().trim();
        String nombre = campoNombre.getText().trim();
        String apellido = campoApellido.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String email = campoEmail.getText().trim();
        String domicilio = campoDomicilio.getText().trim();

        if (cuit.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || email.isEmpty() || domicilio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (clienteService.buscarPorCuit(cuit) != null) {
            JOptionPane.showMessageDialog(this, "El CUIT ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = new Cliente(cuit, nombre, apellido, telefono, email, domicilio);
        clienteService.registrarCliente(cliente);
        JOptionPane.showMessageDialog(this, "Registro exitoso. Ahora puede iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new LoginFrame().setVisible(true);
    }
}
