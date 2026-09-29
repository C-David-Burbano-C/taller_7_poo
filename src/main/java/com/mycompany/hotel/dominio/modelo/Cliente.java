package com.mycompany.hotel.dominio.modelo;

import com.mycompany.hotel.dominio.excepcion.ClienteNoHabilitadoException;
import com.mycompany.hotel.dominio.excepcion.EntidadNoEncontradaException;
import com.mycompany.hotel.dominio.excepcion.HabitacionNoDisponibleException;
import com.mycompany.hotel.dominio.excepcion.TransicionInvalidaException;
import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import com.mycompany.hotel.dominio.puertos.DisponibilidadHabitaciones;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Raíz del agregado Cliente: es dueño de su historial de reservas y de sus penalizaciones.
 * Toda modificación de una reserva pasa por aquí, de modo que la regla
 * "3 penalizaciones ⇒ suspensión" no puede eludirse.
 *
 * <p>No tiene ningún setter; el nombre y el email cambian sólo mediante {@link #modificarDatos}.
 * Estado activo/penalizaciones sólo cambia con verbos de negocio.
 */
public final class Cliente {

    public static final int PENALIZACIONES_PARA_SUSPENDER = 3;
    private static final int LARGO_MAXIMO_NOMBRE = 100;

    private final UUID id;
    private String nombre;
    private Email email;
    private boolean activo;
    private int penalizaciones;
    private final List<Reserva> reservas;

    private Cliente(UUID id, String nombre, Email email, boolean activo, int penalizaciones,
            List<Reserva> reservas) {
        this.id = Requisitos.noNulo(id, "El id del cliente");
        this.nombre = validarNombre(nombre);
        this.email = Requisitos.noNulo(email, "El email del cliente");
        if (penalizaciones < 0) {
            throw new ValidacionDominioException("Las penalizaciones no pueden ser negativas");
        }
        this.activo = activo;
        this.penalizaciones = penalizaciones;
        this.reservas = new ArrayList<>(Requisitos.noNulo(reservas, "Las reservas del cliente"));
    }

    /** Alta de un cliente nuevo: activo y sin penalizaciones. */
    public static Cliente registrar(String nombre, Email email) {
        return new Cliente(UUID.randomUUID(), nombre, email, true, 0, List.of());
    }

    public static Cliente reconstituir(UUID id, String nombre, Email email, boolean activo,
            int penalizaciones, List<Reserva> reservas) {
        return new Cliente(id, nombre, email, activo, penalizaciones, reservas);
    }

    // --- Datos personales ----------------------------------------------------------------

    public void modificarDatos(String nuevoNombre, Email nuevoEmail) {
        String nombreValidado = validarNombre(nuevoNombre);
        Requisitos.noNulo(nuevoEmail, "El nuevo email");
        this.nombre = nombreValidado;
        this.email = nuevoEmail;
    }

    private static String validarNombre(String nombre) {
        String limpio = Requisitos.textoNoVacio(nombre, "El nombre del cliente");
        if (limpio.length() > LARGO_MAXIMO_NOMBRE) {
            throw new ValidacionDominioException(
                    "El nombre no puede superar " + LARGO_MAXIMO_NOMBRE + " caracteres");
        }
        return limpio;
    }

    // --- Ciclo de vida: activo ⇄ suspendido ---------------------------------------------

    public void registrarPenalizacion() {
        this.penalizaciones++;
        if (activo && penalizaciones >= PENALIZACIONES_PARA_SUSPENDER) {
            suspender();
        }
    }

    public void suspender() {
        if (!activo) {
            throw new TransicionInvalidaException("el cliente " + id, "SUSPENDIDO", "suspender");
        }
        this.activo = false;
    }

    public void reactivar() {
        if (activo) {
            throw new TransicionInvalidaException("el cliente " + id, "ACTIVO", "reactivar");
        }
        this.activo = true;
        this.penalizaciones = 0;
    }

    public boolean puedeReservar() {
        return activo;
    }

    // --- Reservas ------------------------------------------------------------------------

    public Reserva reservar(Habitacion habitacion, RangoFechas periodo, PoliticaCancelacion politica,
            DisponibilidadHabitaciones disponibilidad) {
        Requisitos.noNulo(habitacion, "La habitación");
        Requisitos.noNulo(periodo, "El periodo");
        Requisitos.noNulo(politica, "La política de cancelación");
        Requisitos.noNulo(disponibilidad, "La consulta de disponibilidad");
        if (!activo) {
            throw new ClienteNoHabilitadoException(id);
        }
        if (!disponibilidad.estaLibre(habitacion, periodo)) {
            throw new HabitacionNoDisponibleException("La habitación " + habitacion.getNumero().valor()
                    + " ya está reservada entre " + periodo.fechaInicio() + " y " + periodo.fechaFin());
        }
        Reserva nueva = Reserva.crear(id, habitacion, periodo, politica);
        reservas.add(nueva);
        return nueva;
    }

    public Reserva confirmarReserva(UUID reservaId) {
        if (!activo) {
            throw new ClienteNoHabilitadoException(id);
        }
        Reserva reserva = buscarReserva(reservaId);
        reserva.confirmar();
        return reserva;
    }

    /** Cancelar con recargo tardío suma una penalización al cliente. */
    public ResultadoCancelacion cancelarReserva(UUID reservaId, LocalDate hoy) {
        Reserva reserva = buscarReserva(reservaId);
        Dinero recargo = reserva.cancelar(hoy);
        if (recargo.esMayorQueCero()) {
            registrarPenalizacion();
        }
        return new ResultadoCancelacion(reserva, recargo);
    }

    private Reserva buscarReserva(UUID reservaId) {
        Requisitos.noNulo(reservaId, "El id de la reserva");
        return reservas.stream()
                .filter(r -> r.getId().equals(reservaId))
                .findFirst()
                .orElseThrow(() -> new EntidadNoEncontradaException("Reserva", reservaId));
    }

    // --- Derivados calculados por la propia entidad ---------------------------------------

    public int getTotalReservas() {
        return reservas.size();
    }

    /** Suma del costo de las reservas confirmadas. */
    public Dinero getMontoTotalGastado() {
        return reservas.stream()
                .filter(Reserva::estaConfirmada)
                .map(Reserva::getCostoTotal)
                .reduce(Dinero.cero(), Dinero::sumar);
    }

    /** Historial completo, de la reserva más reciente a la más antigua. */
    public List<Reserva> getHistorial() {
        return reservas.stream()
                .sorted(Comparator.comparing((Reserva r) -> r.getPeriodo().fechaInicio()).reversed())
                .toList();
    }

    // --- Lectura -------------------------------------------------------------------------

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Email getEmail() {
        return email;
    }

    public boolean isActivo() {
        return activo;
    }

    public int getPenalizaciones() {
        return penalizaciones;
    }

    /** Vista inmutable: la colección interna nunca se expone. */
    public List<Reserva> getReservas() {
        return List.copyOf(reservas);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Cliente otro && id.equals(otro.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
