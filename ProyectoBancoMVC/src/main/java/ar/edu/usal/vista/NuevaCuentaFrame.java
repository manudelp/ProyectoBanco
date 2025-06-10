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
        super("Apertura de Cuenta - Cliente: " + cliente.getCuit());
        this.cliente = cliente;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 5, 5));

        comboTipo = new JComboBox<>(new String[]{"CajaAhorro", "CuentaCorriente", "Wallet"});
        comboMoneda = new JComboBox<>(Moneda.values());
        comboCripto = new JComboBox<>(Cripto.values());
        campoDescubierto = new JTextField();
        botonCrear = new JButton("Crear Cuenta");

        add(new JLabel("Tipo de cuenta:")); add(comboTipo);
        add(new JLabel("Moneda (para bancarias):")); add(comboMoneda);
        add(new JLabel("Cripto (para Wallet):")); add(comboCripto);
        add(new JLabel("Descubierto (solo CC):")); add(campoDescubierto);
        add(new JLabel()); add(botonCrear);

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
            // Validación de duplicidad
            java.util.List<Cuenta> cuentasCliente = cuentaService.buscarPorCuit(cliente.getCuit());
            if (tipo.equals("CajaAhorro")) {
                Moneda m = (Moneda) comboMoneda.getSelectedItem();
                boolean existe = cuentasCliente.stream().anyMatch(c -> c instanceof CajaAhorro && ((CajaAhorro)c).getMoneda() == m);
                if (existe) {
                    JOptionPane.showMessageDialog(this, "Ya existe una Caja de Ahorro en esa moneda para este cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                CajaAhorro ca = new CajaAhorro(m, cliente.getCuit());
                cuentaService.registrarCuenta(ca);
                identificadorGenerado = ca.getCbu();
            } else if (tipo.equals("CuentaCorriente")) {
                Moneda m = (Moneda) comboMoneda.getSelectedItem();
                boolean existe = cuentasCliente.stream().anyMatch(c -> c instanceof CuentaCorriente && ((CuentaCorriente)c).getMoneda() == m);
                if (existe) {
                    JOptionPane.showMessageDialog(this, "Ya existe una Cuenta Corriente en esa moneda para este cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String descText = campoDescubierto.getText().trim();
                if (descText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El campo Descubierto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double descubierto;
                try {
                    descubierto = Double.parseDouble(descText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El campo Descubierto debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                CuentaCorriente cc = new CuentaCorriente(m, cliente.getCuit(), descubierto);
                cuentaService.registrarCuenta(cc);
                identificadorGenerado = cc.getCbu();
            } else if (tipo.equals("Wallet")) {
                Cripto c = (Cripto) comboCripto.getSelectedItem();
                boolean existe = cuentasCliente.stream().anyMatch(cu -> cu instanceof Wallet && ((Wallet)cu).getCripto() == c);
                if (existe) {
                    JOptionPane.showMessageDialog(this, "Ya existe una Wallet para esa cripto para este cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Wallet w = new Wallet(c);
                cuentaService.registrarCuenta(w);
                identificadorGenerado = w.getDireccion();
            }

            JOptionPane.showMessageDialog(this,
                    "Cuenta creada correctamente.\nIdentificador generado: " + identificadorGenerado,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
