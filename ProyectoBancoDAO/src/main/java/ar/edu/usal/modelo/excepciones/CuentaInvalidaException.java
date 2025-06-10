package ar.edu.usal.modelo.excepciones;

public class CuentaInvalidaException extends RuntimeException {
    public CuentaInvalidaException(String message) {
        super(message);
    }
}
