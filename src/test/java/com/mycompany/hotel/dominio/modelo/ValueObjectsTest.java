package com.mycompany.hotel.dominio.modelo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class ValueObjectsTest {

    // --- Email ---
    @Test void email_rechazaNulo() {
        assertThatThrownBy(() -> new Email(null)).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void email_rechazaVacio() {
        assertThatThrownBy(() -> new Email("  ")).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void email_rechazaFormatoInvalido() {
        assertThatThrownBy(() -> new Email("sin-arroba")).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void email_seNormalizaAMinusculas() {
        assertThat(new Email("  ANA@Correo.COM ").valor()).isEqualTo("ana@correo.com");
    }

    // --- NumeroHabitacion ---
    @Test void numero_rechazaNulo() {
        assertThatThrownBy(() -> new NumeroHabitacion(null)).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void numero_rechazaFormatoInvalido() {
        assertThatThrownBy(() -> new NumeroHabitacion("501")).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void numero_aceptaFormatoValido() {
        assertThat(new NumeroHabitacion("P05-501").valor()).isEqualTo("P05-501");
    }

    // --- CapacidadMaxima ---
    @Test void capacidad_rechazaCero() {
        assertThatThrownBy(() -> new CapacidadMaxima(0)).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void capacidad_rechazaSuperiorAlMaximo() {
        assertThatThrownBy(() -> new CapacidadMaxima(CapacidadMaxima.MAXIMO + 1))
                .isInstanceOf(ValidacionDominioException.class);
    }
    @Test void capacidad_admiteHuespedesDentroDelLimite() {
        CapacidadMaxima c = new CapacidadMaxima(2);
        assertThat(c.admite(2)).isTrue();
        assertThat(c.admite(3)).isFalse();
    }

    // --- RangoFechas ---
    @Test void rango_rechazaInicioNulo() {
        assertThatThrownBy(() -> new RangoFechas(null, LocalDate.now())).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void rango_rechazaFinNulo() {
        assertThatThrownBy(() -> new RangoFechas(LocalDate.now(), null)).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void rango_rechazaFinIgualAlInicio() {
        LocalDate d = LocalDate.of(2026, 5, 1);
        assertThatThrownBy(() -> new RangoFechas(d, d)).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void rango_rechazaFinAnteriorAlInicio() {
        assertThatThrownBy(() -> new RangoFechas(LocalDate.of(2026, 5, 2), LocalDate.of(2026, 5, 1)))
                .isInstanceOf(ValidacionDominioException.class);
    }
    @Test void rango_calculaNoches() {
        assertThat(Fixtures.periodo().noches()).isEqualTo(3);
    }
    @Test void rango_diaDeSalidaNoSolapaConLaSiguienteEntrada() {
        RangoFechas a = new RangoFechas(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 3));
        RangoFechas b = new RangoFechas(LocalDate.of(2026, 5, 3), LocalDate.of(2026, 5, 5));
        RangoFechas c = new RangoFechas(LocalDate.of(2026, 5, 2), LocalDate.of(2026, 5, 4));
        assertThat(a.solapaCon(b)).isFalse();
        assertThat(a.solapaCon(c)).isTrue();
    }

    // --- Dinero ---
    @Test void dinero_rechazaCantidadNula() {
        assertThatThrownBy(() -> new Dinero(null, Dinero.MONEDA_POR_DEFECTO))
                .isInstanceOf(ValidacionDominioException.class);
    }
    @Test void dinero_rechazaMonedaNula() {
        assertThatThrownBy(() -> new Dinero(BigDecimal.ONE, null)).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void dinero_rechazaNegativo() {
        assertThatThrownBy(() -> Dinero.de("-1")).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void dinero_normalizaEscalaParaIgualdad() {
        assertThat(Dinero.de("350")).isEqualTo(Dinero.de("350.00"));
    }
    @Test void dinero_noPierdeCentavosAlSumar() {
        assertThat(Dinero.de("0.10").sumar(Dinero.de("0.20"))).isEqualTo(Dinero.de("0.30"));
    }
    @Test void dinero_rechazaSumarMonedasDistintas() {
        Dinero eur = new Dinero(BigDecimal.ONE, Currency.getInstance("EUR"));
        assertThatThrownBy(() -> Dinero.de("1").sumar(eur)).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void dinero_porcentajeFueraDeRangoEsRechazado() {
        assertThatThrownBy(() -> Dinero.de("10").porcentaje(101)).isInstanceOf(ValidacionDominioException.class);
    }
    @Test void dinero_calculaPorcentaje() {
        assertThat(Dinero.de("200").porcentaje(20)).isEqualTo(Dinero.de("40"));
    }
}
