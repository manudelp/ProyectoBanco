package ar.edu.usal.modelo.persistencia.manager;

import java.io.File;
import java.util.List;

public abstract class GenericStringManager<T> {
    protected File archivo;

    public GenericStringManager(String path) {
        this.archivo = new File(path);
        File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs(); // Crea el directorio si no existe
        }
    }

    public abstract void guardar(T objeto);
    public abstract List<T> leerTodo();
}