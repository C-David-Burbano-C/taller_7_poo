/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema.reservas.dominio.modelo;

/**
 *
 * @author daferarte
 */

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    // Value Object incrustado como columna en la tabla 'clientes'
    @Embedded
    private Email email;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @Column(name = "penalizaciones", nullable = false)
    private int penalizaciones;
    
    // Relación One-to-Many con la entidad Reserva (Lado Inverso: mappedBy apunta al atributo en Reserva)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();

    // Constructor sin argumentos exigido por JPA
    protected Cliente() {}
    
    public Cliente(String nombre, Email email) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        if (email == null) {
            throw new IllegalArgumentException("El email del cliente es obligatorio");
        }
        this.id = UUID.randomUUID();
        this.nombre = nombre;
        this.email = email;
        this.activo = true;
        this.penalizaciones = 0;
    }
    
    public void registrarPenalizacion() {
        this.penalizaciones++;
        // Regla: Si acumula 3 o más penalizaciones, se suspende automáticamente
        if (this.penalizaciones >= 3) {
            this.activo = false;
        }
    }
    
    public void reactivar() {
        this.activo = true;
        this.penalizaciones = 0;
    }
    
    public void actualizarEmail(Email nuevoEmail) {
        if (nuevoEmail == null) {
            throw new IllegalArgumentException("El nuevo email no puede ser nulo");
        }
        this.email = nuevoEmail;
    }

    public boolean puedeRealizarReservas() {
        return this.activo;
    }
    
    // Método de sincronización bidireccional de dominio para la colección
    public void agregarReserva(Reserva reserva) {
        if (!puedeRealizarReservas()) {
            throw new IllegalStateException("El cliente está inactivo y no puede recibir reservas.");
        }
        this.reservas.add(reserva);
    }
    
    // Getters de lectura (Sin setters públicos)
    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public Email getEmail() { return email; }
    public boolean isActivo() { return activo; }
    public int getPenalizaciones() { return penalizaciones; }
    
    // Devolvemos una vista no modificable de la lista para evitar alteraciones externas
    public List<Reserva> getReservas() {
        return Collections.unmodifiableList(reservas);
    }
}
