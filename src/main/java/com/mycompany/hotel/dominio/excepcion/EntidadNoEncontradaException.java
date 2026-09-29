package com.mycompany.hotel.dominio.excepcion;

import java.util.UUID;

/** La entidad solicitada no existe. */
public class EntidadNoEncontradaException extends DominioException {

    public EntidadNoEncontradaException(String entidad, UUID id) {
        super(entidad + " no encontrado(a): " + id);
    }
}
