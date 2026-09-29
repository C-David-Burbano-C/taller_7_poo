package com.mycompany.hotel.dominio.excepcion;

/** Se intentó una transición de estado que el ciclo de vida de la entidad no permite. */
public class TransicionInvalidaException extends ReglaNegocioException {

    public TransicionInvalidaException(String entidad, Object estadoActual, String accion) {
        super("No se puede " + accion + " " + entidad + " en estado " + estadoActual);
    }
}
