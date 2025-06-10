package ar.edu.usal.modelo.excepciones;

public class ConversionNoSoportadaException extends RuntimeException {
    public ConversionNoSoportadaException(String message) {
        super(message);
    }
}
