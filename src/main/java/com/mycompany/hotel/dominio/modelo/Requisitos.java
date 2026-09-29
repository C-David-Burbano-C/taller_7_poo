package com.mycompany.hotel.dominio.modelo;

import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;

/** Guard clauses compartidas por entidades y value objects del dominio. */
final class Requisitos {

    private Requisitos() {
    }

    static <T> T noNulo(T valor, String campo) {
        if (valor == null) {
            throw new ValidacionDominioException(campo + " es obligatorio");
        }
        return valor;
    }

    static String textoNoVacio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ValidacionDominioException(campo + " es obligatorio");
        }
        return valor.trim();
    }
}
