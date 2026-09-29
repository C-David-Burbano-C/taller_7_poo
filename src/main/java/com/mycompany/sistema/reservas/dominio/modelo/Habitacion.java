/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema.reservas.dominio.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 *
 * @author daferarte
 */

@Entity
@Table(name = "habitaciones")
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia normalizada en PostgreSQL
public class Habitacion {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Embedded // Incrusta el Value Object NumeroHabitacion
    private NumeroHabitacion numero;
    @Column(name = "capacidad_maxima", nullable = false)
    private int capacidadMaxima;
    @Column(name = "precio_por_noche", nullable = false)
    private double precioPorNoche;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoHabitacion estado;

    // 1. Constructor protegido sin argumentos exigido por JPA (no accesible fuera del paquete)
    protected Habitacion() {}
    
    public Habitacion(NumeroHabitacion numero, int capacidadMaxima, double precioPorNoche) {
        if (numero == null) {
            throw new IllegalArgumentException("El número de habitación es obligatorio");
        }
        if (capacidadMaxima < 1) {
            throw new IllegalArgumentException("La capacidad máxima debe ser de al menos 1 persona");
        }
        if (precioPorNoche < 0) {
            throw new IllegalArgumentException("El precio por noche no puede ser negativo");
        }
        this.id = UUID.randomUUID();
        this.numero = numero;
        this.capacidadMaxima = capacidadMaxima;
        this.precioPorNoche = precioPorNoche;
        this.estado = EstadoHabitacion.DISPONIBLE;
    }

    public Habitacion(NumeroHabitacion numero, int capacidadMaxima) {
        this(numero, capacidadMaxima, 100.0);
    }

    public void marcarEnMantenimiento() {
        this.estado = EstadoHabitacion.MANTENIMIENTO;
    }

    public void habilitar() {
        this.estado = EstadoHabitacion.DISPONIBLE;
    }

    public void asignarAReserva() {
        if (this.estado == EstadoHabitacion.MANTENIMIENTO) {
            throw new IllegalStateException("No se puede asignar una habitación en mantenimiento");
        }
        if (this.estado == EstadoHabitacion.OCUPADA) {
            throw new IllegalStateException("La habitación ya se encuentra ocupada");
        }
        this.estado = EstadoHabitacion.OCUPADA;
    }

    public UUID getId() { return id; }
    public NumeroHabitacion getNumero() { return numero; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public double getPrecioPorNoche() { return precioPorNoche; }
    public EstadoHabitacion getEstado() { return estado; }
}
