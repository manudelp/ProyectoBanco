package ar.edu.usal.modelo.utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                System.out.println("No se encontró el archivo config.properties en el classpath.");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar config.properties: " + e.getMessage());
        }
    }

    public static String get(String clave) {
        return props.getProperty(clave);
    }
}
