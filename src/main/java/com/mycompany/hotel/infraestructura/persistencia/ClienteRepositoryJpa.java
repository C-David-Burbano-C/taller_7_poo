package com.mycompany.hotel.infraestructura.persistencia;

import com.mycompany.hotel.dominio.modelo.Cliente;
import com.mycompany.hotel.dominio.modelo.Email;
import com.mycompany.hotel.dominio.modelo.Reserva;
import com.mycompany.hotel.dominio.puertos.ClienteRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

/** Adaptador del agregado Cliente: persiste el cliente y sus reservas en una sola operación. */
@Repository
class ClienteRepositoryJpa implements ClienteRepository {

    private final ClienteJpaRepository clientes;
    private final HabitacionJpaRepository habitaciones;

    ClienteRepositoryJpa(ClienteJpaRepository clientes, HabitacionJpaRepository habitaciones) {
        this.clientes = clientes;
        this.habitaciones = habitaciones;
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return clientes.findById(id).map(ClienteRepositoryJpa::aDominio);
    }

    @Override
    public boolean existePorEmail(Email email) {
        return clientes.existsByEmail(email.valor());
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteJpa entidad = clientes.findById(cliente.getId()).orElseGet(() -> new ClienteJpa(cliente.getId()));
        entidad.nombre = cliente.getNombre();
        entidad.email = cliente.getEmail().valor();
        entidad.activo = cliente.isActivo();
        entidad.penalizaciones = cliente.getPenalizaciones();

        Map<UUID, ReservaJpa> existentes = entidad.reservas.stream()
                .collect(Collectors.toMap(r -> r.id, Function.identity()));
        for (Reserva reserva : cliente.getReservas()) {
            ReservaJpa persistida = existentes.get(reserva.getId());
            if (persistida == null) {
                HabitacionJpa habitacion = habitaciones.findById(reserva.getHabitacion().getId()).orElseThrow();
                entidad.reservas.add(new ReservaJpa(reserva, entidad, habitacion));
            } else {
                persistida.estado = reserva.getEstado();
            }
        }
        return aDominio(clientes.save(entidad));
    }

    private static Cliente aDominio(ClienteJpa e) {
        return Cliente.reconstituir(e.id, e.nombre, new Email(e.email), e.activo, e.penalizaciones,
                e.reservas.stream().map(r -> r.aDominio(e.id)).toList());
    }
}

