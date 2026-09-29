package com.mycompany.hotel.aplicacion;

import com.mycompany.hotel.dominio.excepcion.EntidadNoEncontradaException;
import com.mycompany.hotel.dominio.excepcion.ReglaNegocioException;
import com.mycompany.hotel.dominio.modelo.Cliente;
import com.mycompany.hotel.dominio.modelo.Email;
import com.mycompany.hotel.dominio.puertos.ClienteRepository;
import java.util.UUID;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Orquesta casos de uso de clientes; las reglas viven en {@link Cliente}. */
@Service
public class ClienteService {

    private final ClienteRepository clientes;

    public ClienteService(ClienteRepository clientes) {
        this.clientes = clientes;
    }

    @Transactional
    public Cliente registrar(String nombre, String email) {
        Email correo = new Email(email);
        if (clientes.existePorEmail(correo)) {
            throw new ReglaNegocioException("Ya existe un cliente con el email " + correo.valor());
        }
        return clientes.guardar(Cliente.registrar(nombre, correo));
    }

    @Transactional(readOnly = true)
    public Cliente obtener(UUID id) {
        return cargar(id);
    }

    /**
     * Actualización parcial: carga la entidad, deja que {@code aplicador} sobrescriba sólo los
     * campos presentes en el borrador y delega la validación final a la entidad.
     */
    @Transactional
    public Cliente actualizarDatos(UUID id, Consumer<ClienteBorrador> aplicador) {
        Cliente cliente = cargar(id);
        ClienteBorrador borrador = ClienteBorrador.de(cliente);
        aplicador.accept(borrador);
        Email nuevoEmail = new Email(borrador.getEmail());
        if (!nuevoEmail.equals(cliente.getEmail()) && clientes.existePorEmail(nuevoEmail)) {
            throw new ReglaNegocioException("Ya existe un cliente con el email " + nuevoEmail.valor());
        }
        cliente.modificarDatos(borrador.getNombre(), nuevoEmail);
        return clientes.guardar(cliente);
    }

    private Cliente cargar(UUID id) {
        return clientes.buscarPorId(id).orElseThrow(() -> new EntidadNoEncontradaException("Cliente", id));
    }
}
