package com.mycompany.hotel.infraestructura.persistencia;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clientes")
class ClienteJpa {

    @Id
    UUID id;

    @Column(nullable = false, length = 100)
    String nombre;

    @Column(nullable = false, unique = true)
    String email;

    boolean activo;

    int penalizaciones;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ReservaJpa> reservas = new ArrayList<>();

    protected ClienteJpa() {
    }

    ClienteJpa(UUID id) {
        this.id = id;
    }
}

