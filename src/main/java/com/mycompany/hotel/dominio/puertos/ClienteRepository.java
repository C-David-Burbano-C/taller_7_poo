package com.mycompany.hotel.dominio.puertos;

import com.mycompany.hotel.dominio.modelo.Cliente;
import com.mycompany.hotel.dominio.modelo.Email;
import java.util.Optional;
import java.util.UUID;

/** Puerto de persistencia del agregado Cliente (incluye sus reservas). */
public interface ClienteRepository {

    Optional<Cliente> buscarPorId(UUID id);

    boolean existePorEmail(Email email);

    Cliente guardar(Cliente cliente);
}
