package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.entidades.Cuenta;
import ar.edu.usal.modelo.entidades.Transaccion;
import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.ITransaccionService;
import ar.edu.usal.servicios.impl.CuentaService;
import ar.edu.usal.servicios.impl.TransaccionService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExtraccionFrame extends JFrame {

    private final Cliente cliente;
    private final ICuentaService cuentaService;
    private final ITransaccionService transaccionService = new TransaccionService();

    private JComboBox<Cuenta> comboCuentas;
    private JTextField campoMonto;
    private JButton botonExtraer;

    public ExtraccionFrame(Cliente cliente) {
        super("Extracción - Cliente: " + cliente.getCuit());
        this.cliente = cliente;
        this.cuentaService = new CuentaService();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 5, 5));

        comboCuentas = new JComboBox<>();
        cargarCuentas();

        campoMonto = new JTextField();
        botonExtraer = new JButton("Extraer");

        add(new JLabel("Seleccionar cuenta:"));
        add(comboCuentas);
        add(new JLabel("Monto a extraer:"));
        add(campoMonto);
        add(botonExtraer);

        botonExtraer.addActionListener(e -> procesarExtraccion());
    }

    private void cargarCuentas() {
        List<Cuenta> cuentas = cuentaService.buscarPorCuit(cliente.getCuit());
        for (Cuenta c : cuentas) {
            comboCuentas.addItem(c);
        }
    }

    private void procesarExtraccion() {
        Cuenta cuenta = (Cuenta) comboCuentas.getSelectedItem();
        String montoTexto = campoMonto.getText().trim();

        try {
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) throw new NumberFormatException();

            cuenta.extraer(monto);
            cuentaService.actualizarCuenta(cuenta);

            // Registrar transacción
            Transaccion transaccion = new Transaccion(
                    cuenta.getIdentificador(),
                    "EXTRACCION",
                    monto,
                    Transaccion.Tipo.EXTRACCION,
                    cliente.getCuit()
            );
            transaccionService.registrar(transaccion);

            JOptionPane.showMessageDialog(this, "Extracción exitosa.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SaldoInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, "Saldo insuficiente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al extraer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
