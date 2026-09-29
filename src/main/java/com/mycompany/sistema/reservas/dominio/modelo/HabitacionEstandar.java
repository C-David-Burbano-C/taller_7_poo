/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema.reservas.dominio.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 *
 * @author daferarte
 */

@Entity
@Table(name = "habitaciones_estandar")
public class HabitacionEstandar extends Habitacion {
    @Column(name = "camas_individuales", nullable = false)
    private int camasIndividuales;

    protected HabitacionEstandar() {}

    public HabitacionEstandar(NumeroHabitacion numero, int capacidadMaxima, double precioPorNoche, int camasIndividuales) {
        super(numero, capacidadMaxima, precioPorNoche);
        if (camasIndividuales < 0) {
            throw new IllegalArgumentException("El número de camas individuales no puede ser negativo");
        }
        this.camasIndividuales = camasIndividuales;
    }

    public int getCamasIndividuales() { return camasIndividuales; }
}
