package com.mycompany.hotel.dominio.modelo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mycompany.hotel.dominio.excepcion.ClienteNoHabilitadoException;
import com.mycompany.hotel.dominio.excepcion.EntidadNoEncontradaException;
import com.mycompany.hotel.dominio.excepcion.HabitacionNoDisponibleException;
import com.mycompany.hotel.dominio.excepcion.TransicionInvalidaException;
import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import com.mycompany.hotel.dominio.puertos.DisponibilidadHabitaciones;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ClienteTest {

    private static final DisponibilidadHabitaciones LIBRE = (h, p) -> true;
    private static final DisponibilidadHabitaciones OCUPADA = (h, p) -> false;

    private Reserva reservar(Cliente c, PoliticaCancelacion politica) {
        return c.reservar(Fixtures.estandar(), Fixtures.periodo(), politica, LIBRE);
    }

    // --- Constructor: un test por campo ---
    @Test void rechazaNombreNulo() {
        assertThatThrownBy(() -> Cliente.registrar(null, Fixtures.email()))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("nombre");
    }
    @Test void rechazaNombreVacio() {
        assertThatThrownBy(() -> Cliente.registrar("   ", Fixtures.email()))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("nombre");
    }
    @Test void rechazaNombreDemasiadoLargo() {
        assertThatThrownBy(() -> Cliente.registrar("x".repeat(101), Fixtures.email()))
                .isInstanceOf(ValidacionDominioException.class);
    }
    @Test void rechazaEmailNulo() {
        assertThatThrownBy(() -> Cliente.registrar("Ana", null))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("email");
    }

    @Test void naceActivoSinPenalizacionesYConIdGenerado() {
        Cliente c = Fixtures.cliente();
        assertThat(c.isActivo()).isTrue();
        assertThat(c.getPenalizaciones()).isZero();
        assertThat(c.getId()).isNotNull();
        assertThat(c.getReservas()).isEmpty();
    }

    // --- modificarDatos ---
    @Test void modificarDatos_cambiaNombreYEmail() {
        Cliente c = Fixtures.cliente();
        c.modificarDatos("Ana María", new Email("nueva@correo.com"));
        assertThat(c.getNombre()).isEqualTo("Ana María");
        assertThat(c.getEmail().valor()).isEqualTo("nueva@correo.com");
    }
    @Test void modificarDatos_rechazaNombreVacio_yNoAlteraElEstado() {
        Cliente c = Fixtures.cliente();
        assertThatThrownBy(() -> c.modificarDatos(" ", new Email("otro@correo.com")))
                .isInstanceOf(ValidacionDominioException.class);
        assertThat(c.getNombre()).isEqualTo("Ana Torres");
        assertThat(c.getEmail()).isEqualTo(Fixtures.email());
    }
    @Test void modificarDatos_rechazaEmailNulo() {
        assertThatThrownBy(() -> Fixtures.cliente().modificarDatos("Ana", null))
                .isInstanceOf(ValidacionDominioException.class);
    }
    @Test void modificarDatos_noTocaPenalizacionesNiEstado() {
        Cliente c = Fixtures.cliente();
        c.registrarPenalizacion();
        c.modificarDatos("Otro", new Email("otro@correo.com"));
        assertThat(c.getPenalizaciones()).isEqualTo(1);
        assertThat(c.isActivo()).isTrue();
    }

    // --- Suspensión / reactivación ---
    @Test void tresPenalizacionesSuspendenAlCliente() {
        Cliente c = Fixtures.cliente();
        c.registrarPenalizacion();
        c.registrarPenalizacion();
        assertThat(c.isActivo()).isTrue();
        c.registrarPenalizacion();
        assertThat(c.isActivo()).isFalse();
        assertThat(c.puedeReservar()).isFalse();
    }
    @Test void reactivar_desdeSuspendidoReiniciaPenalizaciones() {
        Cliente c = Fixtures.cliente();
        c.suspender();
        c.reactivar();
        assertThat(c.isActivo()).isTrue();
        assertThat(c.getPenalizaciones()).isZero();
    }
    @Test void reactivar_desdeActivoFalla() {
        assertThatThrownBy(Fixtures.cliente()::reactivar).isInstanceOf(TransicionInvalidaException.class);
    }
    @Test void suspender_desdeSuspendidoFalla() {
        Cliente c = Fixtures.cliente();
        c.suspender();
        assertThatThrownBy(c::suspender).isInstanceOf(TransicionInvalidaException.class);
    }
    @Test void suspenderYReactivar_sonSimetricas_cadaUnaFallaSiYaEstaEnElEstadoDestino() {
        Cliente c = Fixtures.cliente();
        assertThatThrownBy(c::reactivar).isInstanceOf(TransicionInvalidaException.class);
        c.suspender();
        assertThatThrownBy(c::suspender).isInstanceOf(TransicionInvalidaException.class);
    }

    // --- Reservar ---
    @Test void reservar_agregaReservaPendienteAlAgregado() {
        Cliente c = Fixtures.cliente();
        Reserva r = reservar(c, PoliticaCancelacion.MODERADA);
        assertThat(r.getEstado()).isEqualTo(EstadoReserva.PENDIENTE);
        assertThat(r.getClienteId()).isEqualTo(c.getId());
        assertThat(c.getTotalReservas()).isEqualTo(1);
    }
    @Test void reservar_clienteSuspendidoFalla() {
        Cliente c = Fixtures.cliente();
        c.suspender();
        assertThatThrownBy(() -> reservar(c, PoliticaCancelacion.MODERADA))
                .isInstanceOf(ClienteNoHabilitadoException.class);
    }
    @Test void reservar_habitacionOcupadaEnEsasFechasFalla() {
        assertThatThrownBy(() -> Fixtures.cliente().reservar(Fixtures.estandar(), Fixtures.periodo(),
                PoliticaCancelacion.MODERADA, OCUPADA)).isInstanceOf(HabitacionNoDisponibleException.class);
    }
    @Test void reservar_rechazaHabitacionNula() {
        assertThatThrownBy(() -> Fixtures.cliente().reservar(null, Fixtures.periodo(), PoliticaCancelacion.MODERADA, LIBRE))
                .isInstanceOf(ValidacionDominioException.class);
    }
    @Test void reservar_rechazaPeriodoNulo() {
        assertThatThrownBy(() -> Fixtures.cliente().reservar(Fixtures.estandar(), null, PoliticaCancelacion.MODERADA, LIBRE))
                .isInstanceOf(ValidacionDominioException.class);
    }

    // --- Confirmar / cancelar ---
    @Test void confirmarReserva_cambiaElEstado() {
        Cliente c = Fixtures.cliente();
        Reserva r = reservar(c, PoliticaCancelacion.MODERADA);
        c.confirmarReserva(r.getId());
        assertThat(r.getEstado()).isEqualTo(EstadoReserva.CONFIRMADA);
    }
    @Test void confirmarReserva_inexistenteFalla() {
        assertThatThrownBy(() -> Fixtures.cliente().confirmarReserva(UUID.randomUUID()))
                .isInstanceOf(EntidadNoEncontradaException.class);
    }
    @Test void confirmarReserva_clienteSuspendidoFalla() {
        Cliente c = Fixtures.cliente();
        Reserva r = reservar(c, PoliticaCancelacion.MODERADA);
        c.suspender();
        assertThatThrownBy(() -> c.confirmarReserva(r.getId())).isInstanceOf(ClienteNoHabilitadoException.class);
    }
    @Test void cancelarSinRecargo_noPenalizaAlCliente() {
        Cliente c = Fixtures.cliente();
        Reserva r = reservar(c, PoliticaCancelacion.ESTRICTA);
        ResultadoCancelacion res = c.cancelarReserva(r.getId(), Fixtures.HOY); // faltan 10 días
        assertThat(res.recargo()).isEqualTo(Dinero.cero());
        assertThat(c.getPenalizaciones()).isZero();
    }
    @Test void cancelarConRecargo_penalizaAlCliente() {
        Cliente c = Fixtures.cliente();
        Reserva r = reservar(c, PoliticaCancelacion.ESTRICTA);
        ResultadoCancelacion res = c.cancelarReserva(r.getId(), LocalDate.of(2026, 1, 8)); // faltan 3 días
        assertThat(res.recargo()).isEqualTo(Dinero.de("120.00"));
        assertThat(c.getPenalizaciones()).isEqualTo(1);
    }
    @Test void cancelarDosVecesFalla_yNoSumaSegundaPenalizacion() {
        Cliente c = Fixtures.cliente();
        Reserva r = reservar(c, PoliticaCancelacion.ESTRICTA);
        c.cancelarReserva(r.getId(), LocalDate.of(2026, 1, 8));
        assertThatThrownBy(() -> c.cancelarReserva(r.getId(), LocalDate.of(2026, 1, 8)))
                .isInstanceOf(TransicionInvalidaException.class);
        assertThat(c.getPenalizaciones()).isEqualTo(1);
    }

    // --- Derivados y encapsulación ---
    @Test void montoTotalGastado_sumaSoloReservasConfirmadas() {
        Cliente c = Fixtures.cliente();
        Reserva confirmada = c.reservar(Fixtures.estandar(), Fixtures.periodo(), PoliticaCancelacion.FLEXIBLE, LIBRE); // 240
        c.reservar(Fixtures.suite(), Fixtures.periodo(), PoliticaCancelacion.FLEXIBLE, LIBRE); // pendiente
        c.confirmarReserva(confirmada.getId());
        Reserva cancelada = c.reservar(Fixtures.suite(), Fixtures.periodo(), PoliticaCancelacion.FLEXIBLE, LIBRE);
        c.cancelarReserva(cancelada.getId(), Fixtures.HOY);
        assertThat(c.getMontoTotalGastado()).isEqualTo(Dinero.de("240.00"));
        assertThat(c.getTotalReservas()).isEqualTo(3);
    }
    @Test void historial_ordenaDeLaMasRecienteALaMasAntigua() {
        Cliente c = Fixtures.cliente();
        Habitacion h = Fixtures.estandar();
        Reserva vieja = c.reservar(h, new RangoFechas(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 3)),
                PoliticaCancelacion.FLEXIBLE, LIBRE);
        Reserva nueva = c.reservar(h, new RangoFechas(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 3)),
                PoliticaCancelacion.FLEXIBLE, LIBRE);
        assertThat(c.getHistorial()).containsExactly(nueva, vieja);
    }
    @Test void getReservas_devuelveVistaInmutable() {
        Cliente c = Fixtures.cliente();
        reservar(c, PoliticaCancelacion.FLEXIBLE);
        assertThatThrownBy(() -> c.getReservas().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(c.getTotalReservas()).isEqualTo(1);
    }
    @Test void igualdadSeBasaSoloEnElId() {
        Cliente a = Fixtures.cliente();
        Cliente mismoId = Cliente.reconstituir(a.getId(), "Otro Nombre", new Email("x@y.com"), false, 2, List.of());
        assertThat(a).isEqualTo(mismoId).hasSameHashCodeAs(mismoId);
        assertThat(a).isNotEqualTo(Fixtures.cliente());
    }
}
