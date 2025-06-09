package ar.edu.usal.modelo.persistencia.manager;

import java.io.*;
import java.util.List;

public abstract class GenericByteManager<T> {
    protected File archivo;

    public GenericByteManager(String path) {
        this.archivo = new File(path);
        File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs(); // Crea el directorio si no existe
        }
    }

    public void guardar(T objeto) {
        try (FileOutputStream fos = new FileOutputStream(archivo, true);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = archivo.length() == 0
                     ? new ObjectOutputStream(bos)
                     : new AppendableObjectOutputStream(bos)) {
            oos.writeObject(objeto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract List<T> leerTodo();

    static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }
        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}