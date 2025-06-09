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
        System.out.println("CuentaDAOImpl cargando desde: " + new File(path).getAbsolutePath());
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
    public void eliminar(Cuenta cuenta) {
        List<Cuenta> cuentas = leerTodo();
        List<Cuenta> actualizadas = new ArrayList<>();

        for (Cuenta c : cuentas) {
            if (cuenta instanceof CajaAhorro && c instanceof CajaAhorro) {
                if (!((CajaAhorro) c).getCbu().equals(((CajaAhorro) cuenta).getCbu())) {
                    actualizadas.add(c);
                }
            } else if (cuenta instanceof CuentaCorriente && c instanceof CuentaCorriente) {
                if (!((CuentaCorriente) c).getCbu().equals(((CuentaCorriente) cuenta).getCbu())) {
                    actualizadas.add(c);
                }
            } else if (cuenta instanceof Wallet && c instanceof Wallet) {
                if (!((Wallet) c).getDireccion().equals(((Wallet) cuenta).getDireccion())) {
                    actualizadas.add(c);
                }
            } else {
                actualizadas.add(c);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
            for (Cuenta c : actualizadas) {
                if (c instanceof CajaAhorro) {
                    CajaAhorro ca = (CajaAhorro) c;
                    writer.write("CA," + ca.getMoneda() + "," + ca.getSaldo() + "," + ca.getCbu() + "," + ca.getCuit());
                } else if (c instanceof CuentaCorriente) {
                    CuentaCorriente cc = (CuentaCorriente) c;
                    writer.write("CC," + cc.getMoneda() + "," + cc.getSaldo() + "," + cc.getCbu() + "," + cc.getCuit() + "," + cc.getDescubierto());
                } else if (c instanceof Wallet) {
                    Wallet w = (Wallet) c;
                    writer.write("WALLET," + w.getMoneda() + "," + w.getSaldo() + "," + w.getDireccion() + "," + w.getTipo());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cuenta> buscarPorCuit(String cuit) {
        List<Cuenta> resultado = new ArrayList<>();
        for (Cuenta cuenta : leerTodo()) {
            if (cuenta instanceof CajaAhorro) {
                CajaAhorro ca = (CajaAhorro) cuenta;
                if (ca.getCuit().equals(cuit)) {
                    resultado.add(ca);
                }
            } else if (cuenta instanceof CuentaCorriente) {
                CuentaCorriente cc = (CuentaCorriente) cuenta;
                if (cc.getCuit().equals(cuit)) {
                    resultado.add(cc);
                }
            }
        }
        return resultado;
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
    public Cuenta buscarPorDireccion(String direccion) {
        List<Cuenta> cuentas = leerTodo();
        for (Cuenta cuenta : cuentas) {
            if (cuenta instanceof Wallet) {
                Wallet w = (Wallet) cuenta;
                if (w.getDireccion().equals(direccion)) return cuenta;
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