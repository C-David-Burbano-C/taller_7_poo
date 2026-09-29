/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Creacionales.builder;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Habitacion;
import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import java.time.LocalDateTime;

/**
 *
 * @author daferarte
 */
public class ReservaBuilder {
    private Cliente cliente;
    private Habitacion habitacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public ReservaBuilder paraCliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public ReservaBuilder enHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
        return this;
    }

    public ReservaBuilder desde(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public ReservaBuilder hasta(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
        return this;
    }

    public Reserva build() {
        if (cliente == null) {
            throw new IllegalStateException("El cliente es obligatorio para construir la reserva");
        }
        if (habitacion == null) {
            throw new IllegalStateException("La habitación es obligatoria para construir la reserva");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalStateException("El rango de fechas debe estar completo");
        }

        RangoFechas periodo = new RangoFechas(fechaInicio, fechaFin);
        return new Reserva(cliente, habitacion, periodo);
    }
}