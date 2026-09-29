package com.mycompany.hotel.dominio.modelo;

import java.time.LocalDate;
import java.util.UUID;

/** Fábricas de objetos válidos para los tests del dominio. */
final class Fixtures {

    static final LocalDate HOY = LocalDate.of(2026, 1, 1);

    private Fixtures() {
    }

    static Email email() {
        return new Email("ana@correo.com");
    }

    static Cliente cliente() {
        return Cliente.registrar("Ana Torres", email());
    }

    static HabitacionEstandar estandar() {
        return HabitacionEstandar.crear(new NumeroHabitacion("P01-101"), Dinero.de("80.00"),
                new CapacidadMaxima(2), 2);
    }

    static SuitePresidencial suite() {
        return SuitePresidencial.crear(new NumeroHabitacion("P05-501"), Dinero.de("350.00"),
                new CapacidadMaxima(2), true, true);
    }

    /** 3 noches, empezando 10 días después de HOY. */
    static RangoFechas periodo() {
        return new RangoFechas(HOY.plusDays(10), HOY.plusDays(13));
    }

    static Reserva reservaPendiente(PoliticaCancelacion politica) {
        return Reserva.crear(UUID.randomUUID(), estandar(), periodo(), politica);
    }
}
