package ar.edu.usal.servicios;

import ar.edu.usal.modelo.entidades.Cuenta;
import ar.edu.usal.modelo.entidades.Transaccion;
import ar.edu.usal.modelo.excepciones.SaldoInsuficienteException;

import java.util.List;

public interface ITransaccionService {
    void registrar(Transaccion t);
    List<Transaccion> listarTodas();
    List<Transaccion> filtrarPorMonto(double minimo);
    void transferir(Cuenta origen, Cuenta destino, double monto) throws SaldoInsuficienteException;
    void convertir(Cuenta origen, Cuenta destino, double monto, String cuitCliente) throws Exception;
}
