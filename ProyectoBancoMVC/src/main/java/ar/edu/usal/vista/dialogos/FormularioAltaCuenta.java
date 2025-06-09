package ar.edu.usal.vista.dialogos;

import ar.edu.usal.controlador.CuentaController;
import ar.edu.usal.modelo.entidades.CriptoTipo;
import ar.edu.usal.modelo.entidades.Moneda;

import javax.swing.*;
import java.awt.*;

public class FormularioAltaCuenta extends JDialog {

    private final CuentaController controller;
    private final Runnable callback;

    private final JTextField txtCuit = new JTextField(15);
    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"CajaAhorro", "CuentaCorriente", "Wallet"});
    private final JComboBox<Moneda> cmbMoneda = new JComboBox<>(Moneda.values());
    private final JTextField txtSaldo = new JTextField(15);
    private final JTextField txtDescubierto = new JTextField(15); // solo para CC
    private final JTextField txtDireccion = new JTextField(15);   // solo para Wallet
    private final JComboBox<CriptoTipo> cmbCripto = new JComboBox<>(CriptoTipo.values()); // solo para Wallet

    public FormularioAltaCuenta(CuentaController controller, Runnable callback) {
        this.controller = controller;
        this.callback = callback;

        setTitle("Alta de Cuenta");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 5, 5));

        panelCampos.add(new JLabel("CUIT del Cliente:"));
        panelCampos.add(txtCuit);

        panelCampos.add(new JLabel("Tipo de Cuenta:"));
        panelCampos.add(cmbTipo);

        panelCampos.add(new JLabel("Moneda:"));
        panelCampos.add(cmbMoneda);

        panelCampos.add(new JLabel("Saldo Inicial:"));
        panelCampos.add(txtSaldo);

        panelCampos.add(new JLabel("Descubierto (solo CC):"));
        panelCampos.add(txtDescubierto);

        panelCampos.add(new JLabel("Dirección (solo Wallet):"));
        panelCampos.add(txtDireccion);

        panelCampos.add(new JLabel("Cripto Tipo (solo Wallet):"));
        panelCampos.add(cmbCripto);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        actualizarVisibilidad(); // estado inicial
        cmbTipo.addActionListener(e -> actualizarVisibilidad());

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void actualizarVisibilidad() {
        String tipo = (String) cmbTipo.getSelectedItem();

        assert tipo != null;
        boolean esWallet = tipo.equals("Wallet");
        boolean esCC = tipo.equals("CuentaCorriente");

        txtDescubierto.setEnabled(esCC);
        txtDireccion.setEnabled(esWallet);
        cmbCripto.setEnabled(esWallet);
    }

    private void guardar() {
        String cuit = txtCuit.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();
        Moneda moneda = (Moneda) cmbMoneda.getSelectedItem();
        String saldoStr = txtSaldo.getText().trim();

        if (cuit.isEmpty() || saldoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CUIT y Saldo son obligatorios.");
            return;
        }

        try {
            double saldo = Double.parseDouble(saldoStr);

            switch (tipo) {
                case "CajaAhorro":
                    controller.crearCajaAhorro(cuit, moneda, saldo);
                    break;
                case "CuentaCorriente":
                    double descubierto = Double.parseDouble(txtDescubierto.getText().trim());
                    controller.crearCuentaCorriente(cuit, moneda, saldo, descubierto);
                    break;
                case "Wallet":
                    String direccion = txtDireccion.getText().trim();
                    CriptoTipo cripto = (CriptoTipo) cmbCripto.getSelectedItem();
                    controller.crearWallet(cuit, cripto, saldo, direccion);
                    break;
            }

            callback.run();
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato numérico inválido.");
        }
    }
}
