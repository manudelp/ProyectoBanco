package ar.edu.usal.test;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.modelo.persistencia.dao.ClienteDAO;
import ar.edu.usal.modelo.persistencia.factory.DAOFactory;

import java.util.Arrays;
import java.util.List;

public class TestClientes {

    public static void main(String[] args) {
        ClienteDAO clienteDAO = DAOFactory.getClienteDAO();

        List<String> nombres = Arrays.asList("Juan", "Lucía", "Marcos", "Elena", "Federico");
        List<String> apellidos = Arrays.asList("Pérez", "Gómez", "Rodríguez", "López", "Martínez");
        List<String> cuits = Arrays.asList("20123456789", "20987654321", "20222333444", "20444555666", "20999988887");
        List<String> telefonos = Arrays.asList("1122334455", "1133445566", "1144556677", "1155667788", "1166778899");
        List<String> emails = Arrays.asList(
                "juan.perez@email.com",
                "lucia.gomez@email.com",
                "marcos.rodriguez@email.com",
                "elena.lopez@email.com",
                "federico.martinez@email.com"
        );
        List<String> domicilios = Arrays.asList(
                "Calle 123, CABA",
                "Av. Siempreviva 742, Buenos Aires",
                "Ruta 8 km 45, Pilar",
                "Diagonal 80 N°1200, La Plata",
                "Bv. Oroño 850, Rosario"
        );


        for (int i = 0; i < cuits.size(); i++) {
            Cliente cliente = new Cliente(
                cuits.get(i),
                nombres.get(i),
                apellidos.get(i),
                telefonos.get(i),
                emails.get(i),
                domicilios.get(i)
            );
            clienteDAO.guardar(cliente);
        }

        System.out.println("Clientes creados correctamente.");
    }
}
