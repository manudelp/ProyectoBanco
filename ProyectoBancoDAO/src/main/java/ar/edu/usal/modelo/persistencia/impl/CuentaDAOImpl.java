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
            writer.write(serializarCuenta(cuenta));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la cuenta: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Cuenta cuenta) {
        List<Cuenta> actualizadas = new ArrayList<>();
        for (Cuenta c : leerTodo()) {
            if (!mismoIdentificador(c, cuenta)) actualizadas.add(c);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
            for (Cuenta c : actualizadas) {
                writer.write(serializarCuenta(c));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la cuenta: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Cuenta cuenta) {
        eliminar(cuenta);
        guardar(cuenta);
    }

    @Override
    public List<Cuenta> leerTodo() {
        List<Cuenta> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] d = linea.split(",");
                if ("CA".equals(d[0])) {
                    lista.add(new CajaAhorro(Double.parseDouble(d[1]), Moneda.valueOf(d[2]), d[3], d[4]));
                } else if ("CC".equals(d[0])) {
                    lista.add(new CuentaCorriente(Double.parseDouble(d[1]), Moneda.valueOf(d[2]), d[3], d[4], Double.parseDouble(d[5])));
                } else if ("WALLET".equals(d[0])) {
                    lista.add(new Wallet(Double.parseDouble(d[1]), d[2], Cripto.valueOf(d[3])));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer las cuentas: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Cuenta> buscarPorCuit(String cuit) {
        List<Cuenta> resultado = new ArrayList<>();
        for (Cuenta cuenta : leerTodo()) {
            if (cuenta instanceof CajaAhorro) {
                CajaAhorro ca = (CajaAhorro) cuenta;
                if (ca.getCuit().equals(cuit)) resultado.add(ca);
            } else if (cuenta instanceof CuentaCorriente) {
                CuentaCorriente cc = (CuentaCorriente) cuenta;
                if (cc.getCuit().equals(cuit)) resultado.add(cc);
            }
        }
        return resultado;
    }

    @Override
    public Cuenta buscarPorCbu(String cbu) {
        for (Cuenta cuenta : leerTodo()) {
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
        for (Cuenta cuenta : leerTodo()) {
            if (cuenta instanceof Wallet) {
                Wallet w = (Wallet) cuenta;
                if (w.getDireccion().equals(direccion)) return w;
            }
        }
        return null;
    }

    private String serializarCuenta(Cuenta cuenta) {
        if (cuenta instanceof CajaAhorro) {
            CajaAhorro ca = (CajaAhorro) cuenta;
            return String.format("CA,%s,%s,%s,%s", ca.getSaldo(), ca.getMoneda(), ca.getCbu(), ca.getCuit());
        } else if (cuenta instanceof CuentaCorriente) {
            CuentaCorriente cc = (CuentaCorriente) cuenta;
            return String.format("CC,%s,%s,%s,%s,%s", cc.getSaldo(), cc.getMoneda(), cc.getCbu(), cc.getCuit(), cc.getDescubierto());
        } else if (cuenta instanceof Wallet) {
            Wallet w = (Wallet) cuenta;
            return String.format("WALLET,%s,%s,%s", w.getSaldo(), w.getDireccion(), w.getCripto());
        }
        throw new IllegalArgumentException("Tipo de cuenta no reconocido");
    }

    private boolean mismoIdentificador(Cuenta a, Cuenta b) {
        return a.getIdentificador().equals(b.getIdentificador());
    }
}
