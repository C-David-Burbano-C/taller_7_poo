package com.mycompany.hotel.dominio.excepcion;

/** Una operación es válida sintácticamente pero contradice una regla de negocio. */
public class ReglaNegocioException extends DominioException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
