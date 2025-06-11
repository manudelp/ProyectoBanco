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
        setSize(450, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Formulario de Registro", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        agregarCampo(formulario, gbc, "CUIT:", campoCuit);
        agregarCampo(formulario, gbc, "Nombre:", campoNombre);
        agregarCampo(formulario, gbc, "Apellido:", campoApellido);
        agregarCampo(formulario, gbc, "Teléfono:", campoTelefono);
        agregarCampo(formulario, gbc, "Email:", campoEmail);
        agregarCampo(formulario, gbc, "Domicilio:", campoDomicilio);

        panelPrincipal.add(formulario, BorderLayout.CENTER);

        JButton botonRegistrar = new JButton("Registrar");
        botonRegistrar.addActionListener(e -> registrarCliente());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(botonRegistrar);
        panelPrincipal.add(acciones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JTextField campo) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(campo, gbc);
        gbc.gridy++;
    }

    private void registrarCliente() {
        String cuit = campoCuit.getText().trim();
        String nombre = campoNombre.getText().trim();
        String apellido = campoApellido.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String email = campoEmail.getText().trim();
        String domicilio = campoDomicilio.getText().trim();

        if (cuit.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                telefono.isEmpty() || email.isEmpty() || domicilio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (clienteService.buscarPorCuit(cuit) != null) {
            JOptionPane.showMessageDialog(this, "El CUIT ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = new Cliente(cuit, nombre, apellido, telefono, email, domicilio);
        clienteService.registrarCliente(cliente);

        JOptionPane.showMessageDialog(this,
                "Registro exitoso. Ahora puede iniciar sesión.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        new LoginFrame().setVisible(true);
    }
}
