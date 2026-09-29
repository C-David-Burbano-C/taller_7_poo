package com.mycompany.hotel.dominio.modelo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mycompany.hotel.dominio.excepcion.HabitacionNoDisponibleException;
import com.mycompany.hotel.dominio.excepcion.TransicionInvalidaException;
import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReservaTest {

    private final UUID clienteId = UUID.randomUUID();

    // --- Constructor: un test por campo ---
    @Test void rechazaClienteNulo() {
        assertThatThrownBy(() -> Reserva.crear(null, Fixtures.estandar(), Fixtures.periodo(), PoliticaCancelacion.FLEXIBLE))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("cliente");
    }
    @Test void rechazaHabitacionNula() {
        assertThatThrownBy(() -> Reserva.crear(clienteId, null, Fixtures.periodo(), PoliticaCancelacion.FLEXIBLE))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("habitación");
    }
    @Test void rechazaPeriodoNulo() {
        assertThatThrownBy(() -> Reserva.crear(clienteId, Fixtures.estandar(), null, PoliticaCancelacion.FLEXIBLE))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("periodo");
    }
    @Test void rechazaPoliticaNula() {
        assertThatThrownBy(() -> Reserva.crear(clienteId, Fixtures.estandar(), Fixtures.periodo(), null))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("política");
    }
    @Test void rechazaHabitacionEnMantenimiento() {
        Habitacion h = Fixtures.estandar();
        h.ponerEnMantenimiento();
        assertThatThrownBy(() -> Reserva.crear(clienteId, h, Fixtures.periodo(), PoliticaCancelacion.FLEXIBLE))
                .isInstanceOf(HabitacionNoDisponibleException.class);
    }

    @Test void nacePendienteConCostoCalculado() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.FLEXIBLE);
        assertThat(r.getEstado()).isEqualTo(EstadoReserva.PENDIENTE);
        assertThat(r.getCostoTotal()).isEqualTo(Dinero.de("240.00"));
        assertThat(r.getId()).isNotNull();
    }

    // --- Transiciones legales ---
    @Test void confirmar_desdePendiente() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.FLEXIBLE);
        r.confirmar();
        assertThat(r.getEstado()).isEqualTo(EstadoReserva.CONFIRMADA);
    }
    @Test void cancelar_desdePendiente() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.FLEXIBLE);
        r.cancelar(Fixtures.HOY);
        assertThat(r.getEstado()).isEqualTo(EstadoReserva.CANCELADA);
        assertThat(r.estaActiva()).isFalse();
    }
    @Test void cancelar_desdeConfirmada() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.FLEXIBLE);
        r.confirmar();
        r.cancelar(Fixtures.HOY);
        assertThat(r.getEstado()).isEqualTo(EstadoReserva.CANCELADA);
    }

    // --- Transiciones ilegales ---
    @Test void confirmar_desdeConfirmadaFalla() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.FLEXIBLE);
        r.confirmar();
        assertThatThrownBy(r::confirmar).isInstanceOf(TransicionInvalidaException.class)
                .hasMessageContaining("CONFIRMADA");
    }
    @Test void confirmar_desdeCanceladaFalla() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.FLEXIBLE);
        r.cancelar(Fixtures.HOY);
        assertThatThrownBy(r::confirmar).isInstanceOf(TransicionInvalidaException.class)
                .hasMessageContaining("CANCELADA");
    }
    @Test void cancelar_desdeCanceladaFalla() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.FLEXIBLE);
        r.cancelar(Fixtures.HOY);
        assertThatThrownBy(() -> r.cancelar(Fixtures.HOY)).isInstanceOf(TransicionInvalidaException.class)
                .hasMessageContaining("CANCELADA");
    }
    @Test void cancelar_rechazaFechaNula() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.FLEXIBLE);
        assertThatThrownBy(() -> r.cancelar(null)).isInstanceOf(ValidacionDominioException.class);
    }

    // --- Simetría confirmar/cancelar desde el estado terminal ---
    @Test void confirmarYCancelar_fallanAmbasDesdeCancelada() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.FLEXIBLE);
        r.cancelar(Fixtures.HOY);
        assertThatThrownBy(r::confirmar).isInstanceOf(TransicionInvalidaException.class);
        assertThatThrownBy(() -> r.cancelar(Fixtures.HOY)).isInstanceOf(TransicionInvalidaException.class);
        assertThat(r.getEstado()).isEqualTo(EstadoReserva.CANCELADA);
    }
    @Test void asimetriaDocumentada_confirmadaNoSeReconfirmaPeroSiSeCancela() {
        assertThat(EstadoReserva.CONFIRMADA.permiteConfirmar()).isFalse();
        assertThat(EstadoReserva.CONFIRMADA.permiteCancelar()).isTrue();
    }

    // --- Recargo ---
    @Test void cancelarConAnticipacionSuficienteNoCobraRecargo() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.ESTRICTA);
        assertThat(r.cancelar(Fixtures.HOY)).isEqualTo(Dinero.cero()); // faltan 10 días
    }
    @Test void cancelarTardeConPoliticaEstrictaDevuelveRecargo() {
        Reserva r = Fixtures.reservaPendiente(PoliticaCancelacion.ESTRICTA);
        assertThat(r.cancelar(LocalDate.of(2026, 1, 8))).isEqualTo(Dinero.de("120.00")); // 50% de 240
    }

    @Test void igualdadSeBasaSoloEnElId() {
        Reserva a = Fixtures.reservaPendiente(PoliticaCancelacion.FLEXIBLE);
        Reserva copia = Reserva.reconstituir(a.getId(), UUID.randomUUID(), Fixtures.suite(), Fixtures.periodo(),
                Dinero.de("1"), PoliticaCancelacion.ESTRICTA, EstadoReserva.CANCELADA);
        assertThat(a).isEqualTo(copia).hasSameHashCodeAs(copia);
    }
}
