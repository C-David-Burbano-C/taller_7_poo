package com.mycompany.hotel.dominio.modelo;

import com.mycompany.hotel.dominio.excepcion.TransicionInvalidaException;
import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import java.util.UUID;

/**
 * Entidad raíz de la jerarquía de habitaciones. Es {@code sealed}: el compilador conoce
 * todos los subtipos, de modo que un subtipo nuevo no pasa desapercibido en los
 * adaptadores que necesitan tratarlo.
 *
 * <p>Ciclo de vida: DISPONIBLE ⇄ OCUPADA, DISPONIBLE ⇄ MANTENIMIENTO.
 * Las transiciones opuestas comparten simetría de guard clauses.
 */
public abstract sealed class Habitacion permits HabitacionEstandar, SuitePresidencial {

    private final UUID id;
    private final NumeroHabitacion numero;
    private final Dinero precioPorNoche;
    private final CapacidadMaxima capacidadMaxima;
    private EstadoHabitacion estado;

    /** Creación de una habitación nueva: id autogenerado, estado inicial fijo. */
    protected Habitacion(NumeroHabitacion numero, Dinero precioPorNoche, CapacidadMaxima capacidadMaxima) {
        this(UUID.randomUUID(), numero, precioPorNoche, capacidadMaxima, EstadoHabitacion.DISPONIBLE);
    }

    /** Rehidratación desde persistencia (sólo la usan las fábricas {@code reconstituir}). */
    protected Habitacion(UUID id, NumeroHabitacion numero, Dinero precioPorNoche,
            CapacidadMaxima capacidadMaxima, EstadoHabitacion estado) {
        this.id = Requisitos.noNulo(id, "El id de la habitación");
        this.numero = Requisitos.noNulo(numero, "El número de habitación");
        this.precioPorNoche = Requisitos.noNulo(precioPorNoche, "El precio por noche");
        if (!precioPorNoche.esMayorQueCero()) {
            throw new ValidacionDominioException("El precio por noche debe ser mayor que cero");
        }
        this.capacidadMaxima = Requisitos.noNulo(capacidadMaxima, "La capacidad máxima");
        this.estado = Requisitos.noNulo(estado, "El estado de la habitación");
    }

    public abstract TipoHabitacion tipo();

    // --- Comportamiento de negocio -------------------------------------------------------

    public void ocupar() {
        exigirEstado(EstadoHabitacion.DISPONIBLE, "ocupar");
        this.estado = EstadoHabitacion.OCUPADA;
    }

    public void liberar() {
        exigirEstado(EstadoHabitacion.OCUPADA, "liberar");
        this.estado = EstadoHabitacion.DISPONIBLE;
    }

    public void ponerEnMantenimiento() {
        exigirEstado(EstadoHabitacion.DISPONIBLE, "poner en mantenimiento");
        this.estado = EstadoHabitacion.MANTENIMIENTO;
    }

    public void habilitar() {
        exigirEstado(EstadoHabitacion.MANTENIMIENTO, "habilitar");
        this.estado = EstadoHabitacion.DISPONIBLE;
    }

    public boolean estaReservable() {
        return estado.esReservable();
    }

    /** El costo de una estadía lo calcula la propia habitación. */
    public Dinero costoPara(RangoFechas periodo) {
        Requisitos.noNulo(periodo, "El periodo");
        return precioPorNoche.multiplicadoPor(periodo.noches());
    }

    private void exigirEstado(EstadoHabitacion requerido, String accion) {
        if (estado != requerido) {
            throw new TransicionInvalidaException("la habitación " + numero.valor(), estado, accion);
        }
    }

    // --- Lectura -------------------------------------------------------------------------

    public UUID getId() {
        return id;
    }

    public NumeroHabitacion getNumero() {
        return numero;
    }

    public Dinero getPrecioPorNoche() {
        return precioPorNoche;
    }

    public CapacidadMaxima getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Habitacion otra && id.equals(otra.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
