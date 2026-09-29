package com.mycompany.hotel.dominio.modelo;

import com.mycompany.hotel.dominio.excepcion.HabitacionNoDisponibleException;
import com.mycompany.hotel.dominio.excepcion.TransicionInvalidaException;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidad hija del agregado {@link Cliente}. Nace PENDIENTE; el costo se fija al crearla
 * (snapshot del precio vigente) y no cambia aunque la tarifa de la habitación sí lo haga.
 *
 * <p>Regla de simetría: {@link #confirmar()} y {@link #cancelar(LocalDate)} fallan ambas
 * desde CANCELADA (estado terminal). Reconfirmar una CONFIRMADA es ilegal, pero cancelarla sí es válido.
 */
public final class Reserva {

    private final UUID id;
    private final UUID clienteId;
    private final Habitacion habitacion;
    private final RangoFechas periodo;
    private final Dinero costoTotal;
    private final PoliticaCancelacion politica;
    private EstadoReserva estado;

    private Reserva(UUID id, UUID clienteId, Habitacion habitacion, RangoFechas periodo, Dinero costoTotal,
            PoliticaCancelacion politica, EstadoReserva estado) {
        this.id = Requisitos.noNulo(id, "El id de la reserva");
        this.clienteId = Requisitos.noNulo(clienteId, "El cliente de la reserva");
        this.habitacion = Requisitos.noNulo(habitacion, "La habitación");
        this.periodo = Requisitos.noNulo(periodo, "El periodo");
        this.costoTotal = Requisitos.noNulo(costoTotal, "El costo total");
        this.politica = Requisitos.noNulo(politica, "La política de cancelación");
        this.estado = Requisitos.noNulo(estado, "El estado de la reserva");
    }

    public static Reserva crear(UUID clienteId, Habitacion habitacion, RangoFechas periodo,
            PoliticaCancelacion politica) {
        Requisitos.noNulo(habitacion, "La habitación");
        Requisitos.noNulo(periodo, "El periodo");
        if (!habitacion.estaReservable()) {
            throw new HabitacionNoDisponibleException(
                    "La habitación " + habitacion.getNumero().valor() + " no está disponible para reservar ("
                    + habitacion.getEstado() + ")");
        }
        return new Reserva(UUID.randomUUID(), clienteId, habitacion, periodo,
                habitacion.costoPara(periodo), politica, EstadoReserva.PENDIENTE);
    }

    public static Reserva reconstituir(UUID id, UUID clienteId, Habitacion habitacion, RangoFechas periodo,
            Dinero costoTotal, PoliticaCancelacion politica, EstadoReserva estado) {
        return new Reserva(id, clienteId, habitacion, periodo, costoTotal, politica, estado);
    }

    public void confirmar() {
        if (!estado.permiteConfirmar()) {
            throw new TransicionInvalidaException("la reserva " + id, estado, "confirmar");
        }
        this.estado = EstadoReserva.CONFIRMADA;
    }

    /**
     * @param hoy fecha de la cancelación (inyectada para que el dominio no dependa del reloj)
     * @return recargo a cobrar según la política de cancelación
     */
    public Dinero cancelar(LocalDate hoy) {
        Requisitos.noNulo(hoy, "La fecha de cancelación");
        if (!estado.permiteCancelar()) {
            throw new TransicionInvalidaException("la reserva " + id, estado, "cancelar");
        }
        Dinero recargo = politica.recargo(costoTotal, periodo.diasHasta(hoy));
        this.estado = EstadoReserva.CANCELADA;
        return recargo;
    }

    public boolean estaActiva() {
        return estado.estaActiva();
    }

    public boolean estaConfirmada() {
        return estado == EstadoReserva.CONFIRMADA;
    }

    public UUID getId() {
        return id;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public RangoFechas getPeriodo() {
        return periodo;
    }

    public Dinero getCostoTotal() {
        return costoTotal;
    }

    public PoliticaCancelacion getPolitica() {
        return politica;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Reserva otra && id.equals(otra.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
