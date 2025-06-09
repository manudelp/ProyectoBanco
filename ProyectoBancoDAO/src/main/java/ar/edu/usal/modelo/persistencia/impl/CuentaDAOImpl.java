package ar.edu.usal.modelo.persistencia.impl;

import ar.edu.usal.modelo.entidades.*;
import ar.edu.usal.modelo.persistencia.dao.CuentaDAO;
import ar.edu.usal.modelo.persistencia.manager.GenericStringManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAOImpl extends GenericStringManager<Cuenta> implements CuentaDAO {

    public CuentaDAOImpl(String path) {
        super(path);
    }

    @Override
    public void guardar(Cuenta cuenta) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
            // Guardar datos de las Cuentas separado por comas
            if (cuenta instanceof CajaAhorro) {
                CajaAhorro ca = (CajaAhorro) cuenta;
                writer.write("CA," +
                        ca.getMoneda() + "," +
                        ca.getSaldo() + "," +
                        ca.getCbu() + "," +
                        ca.getCuit());

            } else if (cuenta instanceof CuentaCorriente) {
                CuentaCorriente cc = (CuentaCorriente) cuenta;
                writer.write("CC," +
                        cc.getMoneda() + "," +
                        cc.getSaldo() + "," +
                        cc.getCbu() + "," +
                        cc.getCuit() + "," +
                        cc.getDescubierto());
            } else if (cuenta instanceof Wallet) {
                Wallet w = (Wallet) cuenta;
                writer.write("WALLET," +
                        w.getMoneda() + "," +
                        w.getSaldo() + "," +
                        w.getDireccion() + "," +
                        w.getTipo());
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cuenta buscarPorCbu(String cbu) {
        List<Cuenta> cuentas = leerTodo();
        for (Cuenta cuenta : cuentas) {
            if (cuenta instanceof CajaAhorro) {
                CajaAhorro ca = (CajaAhorro) cuenta;
                if (ca.getCbu().equals(cbu)) return ca;
            } else if (cuenta instanceof CuentaCorriente) {
                CuentaCorriente cc = (CuentaCorriente) cuenta;
                if (cc.getCbu().equals(cbu)) return cc;
            }
        }
        return null;
    }

    @Override
    public List<Cuenta> leerTodo() {
        List<Cuenta> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Separar datos de la Cuenta
                String[] datos = linea.split(",");

                // Validar tipo de Cuenta
                if (datos[0].equals("CA")) {
                    // Agregar instancia de Cuenta tipo Caja de Ahorro a la lista
                    lista.add(new CajaAhorro(Moneda.valueOf(datos[1]), Double.parseDouble(datos[2]), datos[3], datos[4]));
                } else if (datos[0].equals("CC")) {
                    // Agregar instancia de Cuenta tipo Cuenta Corriente a la lista
                    lista.add(new CuentaCorriente(Moneda.valueOf(datos[1]), Double.parseDouble(datos[2]), datos[3], datos[4], Double.parseDouble(datos[5])));
                } else if (datos[0].equals("WALLET")) {
                    // Agregar instancia de Cuenta tipo Wallet a la lista
                    lista.add(new Wallet(Moneda.valueOf(datos[1]), Double.parseDouble(datos[2]), datos[3], CriptoTipo.valueOf(datos[4])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }
}