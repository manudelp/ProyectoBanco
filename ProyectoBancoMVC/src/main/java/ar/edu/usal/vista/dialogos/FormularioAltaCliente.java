package ar.edu.usal.vista.dialogos;

import ar.edu.usal.controlador.ClienteController;
import ar.edu.usal.modelo.entidades.Cliente;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class FormularioAltaCliente extends JDialog {

    private final JTextField txtCuit = new JTextField(15);
    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtApellido = new JTextField(15);
    private final JTextField txtTelefono = new JTextField(15);
    private final JTextField txtEmail = new JTextField(15);
    private final JTextField txtDomicilio = new JTextField(15);

    private final ClienteController controller;
    private final Consumer<Void> onSuccessCallback;
    private final Cliente clienteExistente;

    public FormularioAltaCliente(ClienteController controller, Consumer<Void> onSuccessCallback) {
        this(controller, onSuccessCallback, null);
    }

    public FormularioAltaCliente(ClienteController controller, Consumer<Void> onSuccessCallback, Cliente clienteExistente) {
        this.controller = controller;
        this.onSuccessCallback = onSuccessCallback;
        this.clienteExistente = clienteExistente;

        setTitle(clienteExistente == null ? "Alta de Cliente" : "Modificación de Cliente");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(350, 350);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));

        panel.add(new JLabel("CUIT:"));
        panel.add(txtCuit);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Domicilio:"));
        panel.add(txtDomicilio);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        if (clienteExistente != null) cargarDatos();

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cargarDatos() {
        txtCuit.setText(clienteExistente.getCuit());
        txtCuit.setEnabled(false); // CUIT no se puede modificar
        txtNombre.setText(clienteExistente.getNombre());
        txtApellido.setText(clienteExistente.getApellido());
        txtTelefono.setText(clienteExistente.getTelefono());
        txtEmail.setText(clienteExistente.getEmail());
        txtDomicilio.setText(clienteExistente.getDomicilio());
    }

    private void guardar() {
        String cuit = txtCuit.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String domicilio = txtDomicilio.getText().trim();

        if (cuit.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                telefono.isEmpty() || email.isEmpty() || domicilio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        Cliente cliente = new Cliente(cuit, nombre, apellido, telefono, email, domicilio);

        if (clienteExistente == null) {
            controller.registrar(cliente);
        } else {
            controller.modificar(cliente);
        }

        onSuccessCallback.accept(null);
        dispose();
    }
}
