package com.mycompany.hotel.infraestructura.persistencia;

import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.HabitacionEstandar;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "habitaciones_estandar")
class HabitacionEstandarJpa extends HabitacionJpa {

    int camasIndividuales;

    protected HabitacionEstandarJpa() {
    }

    HabitacionEstandarJpa(HabitacionEstandar origen) {
        super(origen);
        this.camasIndividuales = origen.getCamasIndividuales();
    }

    @Override
    Habitacion aDominio() {
        return HabitacionEstandar.reconstituir(id, numeroDominio(), precioDominio(), capacidadDominio(),
                estado, camasIndividuales);
    }
}
