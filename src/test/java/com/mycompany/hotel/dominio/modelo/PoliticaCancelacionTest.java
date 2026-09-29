package com.mycompany.hotel.dominio.modelo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PoliticaCancelacionTest {

    private static final Dinero TOTAL = Dinero.de("200.00");

    @Test void flexible_nuncaCobraRecargo() {
        assertThat(PoliticaCancelacion.FLEXIBLE.recargo(TOTAL, 0)).isEqualTo(Dinero.cero());
    }
    @Test void moderada_cobra20PorCientoConMenosDeDosDias() {
        assertThat(PoliticaCancelacion.MODERADA.recargo(TOTAL, 1)).isEqualTo(Dinero.de("40.00"));
    }
    @Test void moderada_noCobraConDosDiasOMas() {
        assertThat(PoliticaCancelacion.MODERADA.recargo(TOTAL, 2)).isEqualTo(Dinero.cero());
    }
    @Test void estricta_cobra50PorCientoConMenosDeSieteDias() {
        assertThat(PoliticaCancelacion.ESTRICTA.recargo(TOTAL, 6)).isEqualTo(Dinero.de("100.00"));
    }
    @Test void estricta_noCobraConSieteDiasOMas() {
        assertThat(PoliticaCancelacion.ESTRICTA.recargo(TOTAL, 7)).isEqualTo(Dinero.cero());
    }
}
