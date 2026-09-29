package com.mycompany.hotel.infraestructura.persistencia;

import com.mycompany.hotel.dominio.modelo.CapacidadMaxima;
import com.mycompany.hotel.dominio.modelo.Dinero;
import com.mycompany.hotel.dominio.modelo.EstadoHabitacion;
import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.HabitacionEstandar;
import com.mycompany.hotel.dominio.modelo.NumeroHabitacion;
import com.mycompany.hotel.dominio.modelo.SuitePresidencial;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

/**
 * Modelo de PERSISTENCIA (deliberadamente simple, no es el modelo de dominio).
 * Herencia mapeada con JOINED: una tabla base + una tabla por subtipo.
 */
@Entity
@Table(name = "habitaciones")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class HabitacionJpa {

    @Id
    UUID id;

    @Column(nullable = false, unique = true, length = 10)
    String numero;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal precioPorNoche;

    @Column(nullable = false, length = 3)
    String moneda;

    @Column(nullable = false)
    int capacidadMaxima;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    EstadoHabitacion estado;

    protected HabitacionJpa() {
    }

    protected HabitacionJpa(Habitacion origen) {
        this.id = origen.getId();
        copiarEstadoDesde(origen);
        this.numero = origen.getNumero().valor();
        this.precioPorNoche = origen.getPrecioPorNoche().cantidad();
        this.moneda = origen.getPrecioPorNoche().moneda().getCurrencyCode();
        this.capacidadMaxima = origen.getCapacidadMaxima().valor();
    }

    final void copiarEstadoDesde(Habitacion origen) {
        this.estado = origen.getEstado();
    }

    /** Polimorfismo: cada subtipo persistido sabe reconstruir su entidad de dominio. */
    abstract Habitacion aDominio();

    final NumeroHabitacion numeroDominio() {
        return new NumeroHabitacion(numero);
    }

    final Dinero precioDominio() {
        return new Dinero(precioPorNoche, Currency.getInstance(moneda));
    }

    final CapacidadMaxima capacidadDominio() {
        return new CapacidadMaxima(capacidadMaxima);
    }

    /** Único punto que enumera los subtipos; al ser {@code sealed}, el compilador exige agregar el nuevo. */
    static HabitacionJpa desde(Habitacion habitacion) {
        return switch (habitacion) {
            case HabitacionEstandar e -> new HabitacionEstandarJpa(e);
            case SuitePresidencial s -> new SuitePresidencialJpa(s);
        };
    }
}
