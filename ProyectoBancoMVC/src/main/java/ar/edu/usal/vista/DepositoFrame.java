package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.ITransaccionService;
import ar.edu.usal.servicios.impl.CuentaService;
import ar.edu.usal.servicios.impl.TransaccionService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DepositoFrame extends JFrame {

    private final Cliente cliente;
    private final ICuentaService cuentaService;
    private final ITransaccionService transaccionService;

    private JComboBox<Cuenta> comboCuentas;
    private JTextField campoMonto;
    private JButton botonDepositar;

    public DepositoFrame(Cliente cliente) {
        super("Depósito");
        this.cliente = cliente;
        this.cuentaService = new CuentaService();
        this.transaccionService = new TransaccionService();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.add(new JLabel("Cliente: " + cliente.getNombre() + " " + cliente.getApellido()));
        info.add(new JLabel("CUIT: " + cliente.getCuit()));
        panelPrincipal.add(info, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(4, 1, 10, 10));
        comboCuentas = new JComboBox<>();
        cargarCuentas();
        campoMonto = new JTextField();

        formulario.add(new JLabel("Seleccionar cuenta:"));
        formulario.add(comboCuentas);
        formulario.add(new JLabel("Monto a depositar:"));
        formulario.add(campoMonto);

        panelPrincipal.add(formulario, BorderLayout.CENTER);

        botonDepositar = new JButton("Depositar");
        botonDepositar.setPreferredSize(new Dimension(120, 30));
        botonDepositar.addActionListener(e -> procesarDeposito());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(botonDepositar);
        panelPrincipal.add(acciones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void cargarCuentas() {
        List<Cuenta> cuentas = cuentaService.buscarPorCuit(cliente.getCuit());
        for (Cuenta cuenta : cuentas) {
            comboCuentas.addItem(cuenta);
        }
    }

    private void procesarDeposito() {
        Cuenta cuenta = (Cuenta) comboCuentas.getSelectedItem();
        String montoTexto = campoMonto.getText().trim();

        try {
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) throw new NumberFormatException();

            cuenta.depositar(monto);
            cuentaService.actualizarCuenta(cuenta);

            Transaccion transaccion = new Transaccion(
                    cuenta.getIdentificador(),
                    "DEPOSITO",
                    monto,
                    cuenta instanceof CajaAhorro ? ((CajaAhorro) cuenta).getMoneda().name() :
                            cuenta instanceof CuentaCorriente ? ((CuentaCorriente) cuenta).getMoneda().name() :
                                    cuenta instanceof Wallet ? ((Wallet) cuenta).getCripto().name() :
                                            "DESCONOCIDO",
                    Transaccion.Tipo.DEPOSITO,
                    cliente.getCuit()
            );
            transaccionService.registrar(transaccion);

            JOptionPane.showMessageDialog(this, "Depósito exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al depositar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
