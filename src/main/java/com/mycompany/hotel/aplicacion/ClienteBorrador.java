package com.mycompany.hotel.aplicacion;

import com.mycompany.hotel.dominio.modelo.Cliente;

/**
 * Borrador mutable con SÓLO los datos editables de un cliente (nombre y email).
 * Permite que MapStruct aplique una actualización parcial ({@code IGNORE} en nulos) sin
 * exponer setters en la entidad rica. Como no contiene id, activo, penalizaciones ni reservas,
 * esos campos son inmunes a la sobre-asignación (mass assignment) por construcción.
 * Los valores finales los valida la entidad en {@link Cliente#modificarDatos}.
 */
public class ClienteBorrador {

    private String nombre;
    private String email;

    public static ClienteBorrador de(Cliente cliente) {
        ClienteBorrador borrador = new ClienteBorrador();
        borrador.nombre = cliente.getNombre();
        borrador.email = cliente.getEmail().valor();
        return borrador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
