package com.mycompany.hotel.infraestructura.persistencia;

import com.mycompany.hotel.dominio.modelo.Dinero;
import com.mycompany.hotel.dominio.modelo.EstadoReserva;
import com.mycompany.hotel.dominio.modelo.PoliticaCancelacion;
import com.mycompany.hotel.dominio.modelo.RangoFechas;
import com.mycompany.hotel.dominio.modelo.Reserva;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

@Entity
@Table(name = "reservas")
class ReservaJpa {

    @Id
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    ClienteJpa cliente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "habitacion_id")
    HabitacionJpa habitacion;

    @Column(nullable = false)
    LocalDate fechaInicio;

    @Column(nullable = false)
    LocalDate fechaFin;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal costoTotal;

    @Column(nullable = false, length = 3)
    String moneda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    PoliticaCancelacion politica;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    EstadoReserva estado;

    protected ReservaJpa() {
    }

    ReservaJpa(Reserva origen, ClienteJpa cliente, HabitacionJpa habitacion) {
        this.id = origen.getId();
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaInicio = origen.getPeriodo().fechaInicio();
        this.fechaFin = origen.getPeriodo().fechaFin();
        this.costoTotal = origen.getCostoTotal().cantidad();
        this.moneda = origen.getCostoTotal().moneda().getCurrencyCode();
        this.politica = origen.getPolitica();
        this.estado = origen.getEstado();
    }

    Reserva aDominio(UUID clienteId) {
        return Reserva.reconstituir(id, clienteId, habitacion.aDominio(), new RangoFechas(fechaInicio, fechaFin),
                new Dinero(costoTotal, Currency.getInstance(moneda)), politica, estado);
    }
}

