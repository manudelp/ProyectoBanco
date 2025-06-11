package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.impl.CuentaService;

import javax.swing.*;
import java.awt.*;

public class NuevaCuentaFrame extends JFrame {

    private final Cliente cliente;
    private final ICuentaService cuentaService = new CuentaService();

    private JComboBox<String> comboTipo;
    private JComboBox<Moneda> comboMoneda;
    private JComboBox<Cripto> comboCripto;
    private JTextField campoDescubierto;
    private JButton botonCrear;

    public NuevaCuentaFrame(Cliente cliente) {
        super("Apertura de Cuenta");
        this.cliente = cliente;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Info cliente
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.add(new JLabel("Cliente: " + cliente.getNombre() + " " + cliente.getApellido()));
        info.add(new JLabel("CUIT: " + cliente.getCuit()));
        panelPrincipal.add(info, BorderLayout.NORTH);

        // Formulario
        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        comboTipo = new JComboBox<>(new String[]{"CajaAhorro", "CuentaCorriente", "Wallet"});
        comboMoneda = new JComboBox<>(Moneda.values());
        comboCripto = new JComboBox<>(Cripto.values());
        campoDescubierto = new JTextField();
        botonCrear = new JButton("Crear Cuenta");

        formulario.add(new JLabel("Tipo de cuenta:"), gbc);
        gbc.gridx = 1;
        formulario.add(comboTipo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formulario.add(new JLabel("Moneda (para bancarias):"), gbc);
        gbc.gridx = 1;
        formulario.add(comboMoneda, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formulario.add(new JLabel("Cripto (para Wallet):"), gbc);
        gbc.gridx = 1;
        formulario.add(comboCripto, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formulario.add(new JLabel("Descubierto (solo CC):"), gbc);
        gbc.gridx = 1;
        formulario.add(campoDescubierto, gbc);

        panelPrincipal.add(formulario, BorderLayout.CENTER);

        // Botón abajo
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(botonCrear);
        panelPrincipal.add(acciones, BorderLayout.SOUTH);

        add(panelPrincipal);

        actualizarVisibilidad();
        comboTipo.addActionListener(e -> actualizarVisibilidad());
        botonCrear.addActionListener(e -> crearCuenta());
    }

    private void actualizarVisibilidad() {
        String tipo = (String) comboTipo.getSelectedItem();
        boolean esWallet = tipo.equals("Wallet");
        comboMoneda.setEnabled(!esWallet);
        comboCripto.setEnabled(esWallet);
        campoDescubierto.setEnabled(tipo.equals("CuentaCorriente"));
    }

    private void crearCuenta() {
        try {
            String tipo = (String) comboTipo.getSelectedItem();
            String identificadorGenerado = "";
            var cuentasCliente = cuentaService.buscarPorCuit(cliente.getCuit());

            if (tipo.equals("CajaAhorro")) {
                Moneda m = (Moneda) comboMoneda.getSelectedItem();
                boolean existe = cuentasCliente.stream().anyMatch(c -> c instanceof CajaAhorro && ((CajaAhorro) c).getMoneda() == m);
                if (existe) {
                    mostrarError("Ya existe una Caja de Ahorro en esa moneda para este cliente.");
                    return;
                }
                CajaAhorro ca = new CajaAhorro(m, cliente.getCuit());
                cuentaService.registrarCuenta(ca);
                cliente.agregarCuenta(ca);
                identificadorGenerado = ca.getCbu();

            } else if (tipo.equals("CuentaCorriente")) {
                Moneda m = (Moneda) comboMoneda.getSelectedItem();
                boolean existe = cuentasCliente.stream().anyMatch(c -> c instanceof CuentaCorriente && ((CuentaCorriente) c).getMoneda() == m);
                if (existe) {
                    mostrarError("Ya existe una Cuenta Corriente en esa moneda para este cliente.");
                    return;
                }
                String descText = campoDescubierto.getText().trim();
                if (descText.isEmpty()) {
                    mostrarError("El campo Descubierto no puede estar vacío.");
                    return;
                }
                double descubierto;
                try {
                    descubierto = Double.parseDouble(descText);
                } catch (NumberFormatException ex) {
                    mostrarError("El campo Descubierto debe ser numérico.");
                    return;
                }
                CuentaCorriente cc = new CuentaCorriente(m, cliente.getCuit(), descubierto);
                cuentaService.registrarCuenta(cc);
                cliente.agregarCuenta(cc);
                identificadorGenerado = cc.getCbu();

            } else if (tipo.equals("Wallet")) {
                Cripto c = (Cripto) comboCripto.getSelectedItem();
                boolean existe = cuentasCliente.stream().anyMatch(cu -> cu instanceof Wallet && ((Wallet) cu).getCripto() == c);
                if (existe) {
                    mostrarError("Ya existe una Wallet para esa cripto para este cliente.");
                    return;
                }
                Wallet w = new Wallet(c, cliente.getCuit());
                cuentaService.registrarCuenta(w);
                cliente.agregarCuenta(w);
                identificadorGenerado = w.getDireccion();
            }

            JOptionPane.showMessageDialog(this,
                    "Cuenta creada correctamente.\nIdentificador generado: " + identificadorGenerado,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            mostrarError("Error: " + ex.getMessage());
        }
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
