package com.mycompany.hotel.dominio.modelo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mycompany.hotel.dominio.excepcion.TransicionInvalidaException;
import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class HabitacionTest {

    private final NumeroHabitacion numero = new NumeroHabitacion("P01-101");
    private final Dinero precio = Dinero.de("80.00");
    private final CapacidadMaxima capacidad = new CapacidadMaxima(2);

    // --- Constructor: un test por campo ---
    @Test void rechazaNumeroNulo() {
        assertThatThrownBy(() -> HabitacionEstandar.crear(null, precio, capacidad, 1))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("número");
    }
    @Test void rechazaPrecioNulo() {
        assertThatThrownBy(() -> HabitacionEstandar.crear(numero, null, capacidad, 1))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("precio");
    }
    @Test void rechazaPrecioCero() {
        assertThatThrownBy(() -> HabitacionEstandar.crear(numero, Dinero.cero(), capacidad, 1))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("precio");
    }
    @Test void rechazaCapacidadNula() {
        assertThatThrownBy(() -> HabitacionEstandar.crear(numero, precio, null, 1))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("capacidad");
    }
    @Test void estandar_rechazaCeroCamas() {
        assertThatThrownBy(() -> HabitacionEstandar.crear(numero, precio, capacidad, 0))
                .isInstanceOf(ValidacionDominioException.class).hasMessageContaining("cama");
    }
    @Test void suite_rechazaNumeroNulo() {
        assertThatThrownBy(() -> SuitePresidencial.crear(null, precio, capacidad, true, true))
                .isInstanceOf(ValidacionDominioException.class);
    }
    @Test void suite_rechazaPrecioNulo() {
        assertThatThrownBy(() -> SuitePresidencial.crear(numero, null, capacidad, true, true))
                .isInstanceOf(ValidacionDominioException.class);
    }
    @Test void suite_rechazaCapacidadNula() {
        assertThatThrownBy(() -> SuitePresidencial.crear(numero, precio, null, true, true))
                .isInstanceOf(ValidacionDominioException.class);
    }

    // --- Estado inicial e identidad ---
    @Test void nacePorDefectoDisponibleConIdGenerado() {
        Habitacion h = Fixtures.estandar();
        assertThat(h.getEstado()).isEqualTo(EstadoHabitacion.DISPONIBLE);
        assertThat(h.getId()).isNotNull();
        assertThat(Fixtures.estandar().getId()).isNotEqualTo(h.getId());
    }
    @Test void tipoDependeDelSubtipo() {
        assertThat(Fixtures.estandar().tipo()).isEqualTo(TipoHabitacion.ESTANDAR);
        assertThat(Fixtures.suite().tipo()).isEqualTo(TipoHabitacion.SUITE);
    }
    @Test void suiteConservaSusAtributosPropios() {
        SuitePresidencial s = Fixtures.suite();
        assertThat(s.isIncluyeMayordomo()).isTrue();
        assertThat(s.isJacuzziPrivado()).isTrue();
    }
    @Test void igualdadSeBasaSoloEnElId() {
        UUID id = UUID.randomUUID();
        HabitacionEstandar a = HabitacionEstandar.reconstituir(id, numero, precio, capacidad, EstadoHabitacion.DISPONIBLE, 1);
        HabitacionEstandar b = HabitacionEstandar.reconstituir(id, new NumeroHabitacion("P09-909"),
                Dinero.de("1.00"), capacidad, EstadoHabitacion.OCUPADA, 3);
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(Fixtures.estandar());
    }
    @Test void costoParaLoCalculaLaPropiaHabitacion() {
        assertThat(Fixtures.estandar().costoPara(Fixtures.periodo())).isEqualTo(Dinero.de("240.00"));
    }

    // --- Transiciones legales ---
    @Test void ocupar_desdeDisponible() {
        Habitacion h = Fixtures.estandar();
        h.ocupar();
        assertThat(h.getEstado()).isEqualTo(EstadoHabitacion.OCUPADA);
    }
    @Test void liberar_desdeOcupada() {
        Habitacion h = Fixtures.estandar();
        h.ocupar();
        h.liberar();
        assertThat(h.getEstado()).isEqualTo(EstadoHabitacion.DISPONIBLE);
    }
    @Test void ponerEnMantenimiento_desdeDisponible() {
        Habitacion h = Fixtures.estandar();
        h.ponerEnMantenimiento();
        assertThat(h.getEstado()).isEqualTo(EstadoHabitacion.MANTENIMIENTO);
        assertThat(h.estaReservable()).isFalse();
    }
    @Test void habilitar_desdeMantenimiento() {
        Habitacion h = Fixtures.estandar();
        h.ponerEnMantenimiento();
        h.habilitar();
        assertThat(h.getEstado()).isEqualTo(EstadoHabitacion.DISPONIBLE);
    }

    // --- Transiciones ilegales ---
    @Test void ocupar_desdeOcupadaFalla() {
        Habitacion h = Fixtures.estandar();
        h.ocupar();
        assertThatThrownBy(h::ocupar).isInstanceOf(TransicionInvalidaException.class);
    }
    @Test void ocupar_desdeMantenimientoFalla() {
        Habitacion h = Fixtures.estandar();
        h.ponerEnMantenimiento();
        assertThatThrownBy(h::ocupar).isInstanceOf(TransicionInvalidaException.class);
    }
    @Test void liberar_desdeDisponibleFalla() {
        assertThatThrownBy(Fixtures.estandar()::liberar).isInstanceOf(TransicionInvalidaException.class);
    }
    @Test void habilitar_desdeDisponibleFalla() {
        assertThatThrownBy(Fixtures.estandar()::habilitar).isInstanceOf(TransicionInvalidaException.class);
    }
    @Test void ponerEnMantenimiento_desdeOcupadaFalla() {
        Habitacion h = Fixtures.estandar();
        h.ocupar();
        assertThatThrownBy(h::ponerEnMantenimiento).isInstanceOf(TransicionInvalidaException.class);
    }

    // --- Simetría ---
    @Test void ocuparYMantenimiento_fallanAmbasDesdeUnEstadoNoDisponible() {
        Habitacion ocupada = Fixtures.estandar();
        ocupada.ocupar();
        assertThatThrownBy(ocupada::ocupar).isInstanceOf(TransicionInvalidaException.class);
        assertThatThrownBy(ocupada::ponerEnMantenimiento).isInstanceOf(TransicionInvalidaException.class);

        Habitacion enMantenimiento = Fixtures.estandar();
        enMantenimiento.ponerEnMantenimiento();
        assertThatThrownBy(enMantenimiento::ocupar).isInstanceOf(TransicionInvalidaException.class);
        assertThatThrownBy(enMantenimiento::ponerEnMantenimiento).isInstanceOf(TransicionInvalidaException.class);
    }
}
