package com.mycompany.hotel.dominio.puertos;

import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.NumeroHabitacion;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de persistencia de habitaciones (todas las variantes de la jerarquía). */
public interface HabitacionRepository {

    Optional<Habitacion> buscarPorId(UUID id);

    List<Habitacion> buscarTodas();

    boolean existePorNumero(NumeroHabitacion numero);

    Habitacion guardar(Habitacion habitacion);
}
