package ar.edu.usal.controlador;

import ar.edu.usal.modelo.entidades.Cliente;
import ar.edu.usal.servicios.IClienteService;
import ar.edu.usal.servicios.impl.ClienteService;

public class LoginController {

    private final IClienteService clienteService;

    public LoginController() {
        this.clienteService = new ClienteService();
    }

    /**
     * Valida si el CUIT ingresado corresponde a un cliente registrado.
     * @param cuit CUIT ingresado
     * @return Cliente si es válido, null si no existe
     */
    public Cliente validarCuit(String cuit) {
        return clienteService.buscarPorCuit(cuit);
    }
}
