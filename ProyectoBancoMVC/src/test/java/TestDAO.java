import ar.edu.usal.modelo.entidades.Cliente;

public class TestDAO {
    public static void main(String[] args) {
        Cliente cliente = new Cliente("20123456789", "Juan", "Pérez", "1112349876", "mail@mail.com", "Casa");
        System.out.println("Cliente: " + cliente.getNombre());
    }
}
