package com.mycompany.hotel.infraestructura.persistencia;

import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.NumeroHabitacion;
import com.mycompany.hotel.dominio.puertos.HabitacionRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/** Adaptador: implementa el puerto del dominio sobre Spring Data JPA. */
@Repository
class HabitacionRepositoryJpa implements HabitacionRepository {

    private final HabitacionJpaRepository jpa;

    HabitacionRepositoryJpa(HabitacionJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Habitacion> buscarPorId(UUID id) {
        return jpa.findById(id).map(HabitacionJpa::aDominio);
    }

    @Override
    public List<Habitacion> buscarTodas() {
        return jpa.findAll().stream().map(HabitacionJpa::aDominio).toList();
    }

    @Override
    public boolean existePorNumero(NumeroHabitacion numero) {
        return jpa.existsByNumero(numero.valor());
    }

    @Override
    public Habitacion guardar(Habitacion habitacion) {
        HabitacionJpa entidad = jpa.findById(habitacion.getId()).orElseGet(() -> HabitacionJpa.desde(habitacion));
        entidad.copiarEstadoDesde(habitacion);
        return jpa.save(entidad).aDominio();
    }
}
