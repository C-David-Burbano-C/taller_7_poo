package com.mycompany.hotel.dominio.modelo;

import java.util.UUID;

/** Suite presidencial: se distingue por servicios exclusivos (mayordomo y jacuzzi privado). */
public final class SuitePresidencial extends Habitacion {

    private final boolean incluyeMayordomo;
    private final boolean jacuzziPrivado;

    private SuitePresidencial(NumeroHabitacion numero, Dinero precio, CapacidadMaxima capacidad,
            boolean incluyeMayordomo, boolean jacuzziPrivado) {
        super(numero, precio, capacidad);
        this.incluyeMayordomo = incluyeMayordomo;
        this.jacuzziPrivado = jacuzziPrivado;
    }

    private SuitePresidencial(UUID id, NumeroHabitacion numero, Dinero precio, CapacidadMaxima capacidad,
            EstadoHabitacion estado, boolean incluyeMayordomo, boolean jacuzziPrivado) {
        super(id, numero, precio, capacidad, estado);
        this.incluyeMayordomo = incluyeMayordomo;
        this.jacuzziPrivado = jacuzziPrivado;
    }

    public static SuitePresidencial crear(NumeroHabitacion numero, Dinero precioPorNoche,
            CapacidadMaxima capacidadMaxima, boolean incluyeMayordomo, boolean jacuzziPrivado) {
        return new SuitePresidencial(numero, precioPorNoche, capacidadMaxima, incluyeMayordomo, jacuzziPrivado);
    }

    public static SuitePresidencial reconstituir(UUID id, NumeroHabitacion numero, Dinero precioPorNoche,
            CapacidadMaxima capacidadMaxima, EstadoHabitacion estado,
            boolean incluyeMayordomo, boolean jacuzziPrivado) {
        return new SuitePresidencial(id, numero, precioPorNoche, capacidadMaxima, estado,
                incluyeMayordomo, jacuzziPrivado);
    }

    @Override
    public TipoHabitacion tipo() {
        return TipoHabitacion.SUITE;
    }

    public boolean isIncluyeMayordomo() {
        return incluyeMayordomo;
    }

    public boolean isJacuzziPrivado() {
        return jacuzziPrivado;
    }
}
