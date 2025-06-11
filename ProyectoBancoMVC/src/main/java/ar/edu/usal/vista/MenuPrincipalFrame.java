package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.servicios.ICuentaService;
import ar.edu.usal.servicios.impl.CuentaService;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private final Cliente cliente;
    private JLabel datos;
    private final ICuentaService cuentaService = new CuentaService();

    public MenuPrincipalFrame(Cliente cliente) {
        super("Menú Principal - Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
        this.cliente = cliente;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel infoCliente = new JPanel(new GridLayout(2, 1));
        JLabel saludo = new JLabel("Bienvenido, " + cliente.getNombre() + " " + cliente.getApellido());
        saludo.setFont(new Font("SansSerif", Font.BOLD, 16));
        datos = new JLabel();
        datos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        actualizarDatosCliente();
        infoCliente.add(saludo);
        infoCliente.add(datos);
        panelPrincipal.add(infoCliente, BorderLayout.NORTH);

        JPanel panelAcciones = new JPanel(new GridLayout(2, 3, 10, 10));
        agregarBoton(panelAcciones, "Depositar", () -> new DepositoFrame(cliente).setVisible(true));
        agregarBoton(panelAcciones, "Extraer", () -> new ExtraccionFrame(cliente).setVisible(true));
        agregarBoton(panelAcciones, "Transferir", () -> new TransferenciaFrame(cliente).setVisible(true));
        agregarBoton(panelAcciones, "Convertir", () -> new ConversionFrame(cliente).setVisible(true));
        agregarBoton(panelAcciones, "Ver Transacciones", () -> new TransaccionesFrame(cliente).setVisible(true));
        agregarBoton(panelAcciones, "Abrir Cuenta", () -> new NuevaCuentaFrame(cliente).setVisible(true));
        panelPrincipal.add(panelAcciones, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonSalir = new JButton("Salir");
        botonSalir.setPreferredSize(new Dimension(100, 30));
        botonSalir.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        panelInferior.add(botonSalir);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);

        addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                actualizarDatosCliente();
            }
        });
    }

    private void agregarBoton(JPanel panel, String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(150, 40));
        boton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        boton.addActionListener(e -> accion.run());
        panel.add(boton);
    }

    private void actualizarDatosCliente() {
        int cuentasActivas = cuentaService.buscarPorCuit(cliente.getCuit()).size();
        datos.setText("CUIT: " + cliente.getCuit() + " | Cuentas activas: " + cuentasActivas);
    }
}
