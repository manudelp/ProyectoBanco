package ar.edu.usal.vista;

import ar.edu.usal.modelo.entidades.Cliente;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private final Cliente cliente;

    public MenuPrincipalFrame(Cliente cliente) {
        super("Menú Principal - Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
        this.cliente = cliente;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 3, 10, 10));

        agregarBoton("Depositar", () -> new DepositoFrame(cliente).setVisible(true));
        agregarBoton("Extraer", () -> new ExtraccionFrame(cliente).setVisible(true));
        agregarBoton("Transferir", () -> new TransferenciaFrame(cliente).setVisible(true));
        agregarBoton("Convertir", () -> new ConversionFrame(cliente).setVisible(true));
        agregarBoton("Ver Transacciones", () -> new TransaccionesFrame(cliente).setVisible(true));
        agregarBoton("Abrir Cuenta", () -> new NuevaCuentaFrame(cliente).setVisible(true));
        agregarBoton("Salir", () -> System.exit(0));
    }

    private void agregarBoton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> accion.run());
        add(boton);
    }
}
