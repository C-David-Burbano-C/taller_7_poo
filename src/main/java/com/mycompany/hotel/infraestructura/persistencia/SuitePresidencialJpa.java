package com.mycompany.hotel.infraestructura.persistencia;

import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.SuitePresidencial;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "suites_presidenciales")
class SuitePresidencialJpa extends HabitacionJpa {

    boolean incluyeMayordomo;
    boolean jacuzziPrivado;

    protected SuitePresidencialJpa() {
    }

    SuitePresidencialJpa(SuitePresidencial origen) {
        super(origen);
        this.incluyeMayordomo = origen.isIncluyeMayordomo();
        this.jacuzziPrivado = origen.isJacuzziPrivado();
    }

    @Override
    Habitacion aDominio() {
        return SuitePresidencial.reconstituir(id, numeroDominio(), precioDominio(), capacidadDominio(),
                estado, incluyeMayordomo, jacuzziPrivado);
    }
}
