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
@Table(name = "suites_presidenciales")
public class SuitePresidencial extends Habitacion {
    @Column(name = "incluye_mayordomo", nullable = false)
    private boolean incluyeMayordomo;

    @Column(name = "jacuzzi_privado", nullable = false)
    private boolean jacuzziPrivado;

    protected SuitePresidencial() {}

    public SuitePresidencial(NumeroHabitacion numero, int capacidadMaxima, double precioPorNoche, boolean incluyeMayordomo, boolean jacuzziPrivado) {
        super(numero, capacidadMaxima, precioPorNoche);
        this.incluyeMayordomo = incluyeMayordomo;
        this.jacuzziPrivado = jacuzziPrivado;
    }

    public boolean isIncluyeMayordomo() { return incluyeMayordomo; }
    public boolean isJacuzziPrivado() { return jacuzziPrivado; }
}
