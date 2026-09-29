package com.mycompany.hotel.dominio.modelo;

import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;

/** Value Object: número máximo de huéspedes de una habitación (1 a 10). */
public record CapacidadMaxima(int valor) {

    public static final int MAXIMO = 10;

    public CapacidadMaxima {
        if (valor < 1 || valor > MAXIMO) {
            throw new ValidacionDominioException(
                    "La capacidad máxima debe estar entre 1 y " + MAXIMO + ": " + valor);
        }
    }

    public boolean admite(int huespedes) {
        return huespedes >= 1 && huespedes <= valor;
    }
}
