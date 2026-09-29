package com.mycompany.hotel.dominio.excepcion;

/**
 * Raíz de la jerarquía de excepciones del dominio. Permite a las capas
 * externas (web) traducir cualquier violación de reglas de negocio sin
 * depender de excepciones genéricas de la JDK.
 */
public abstract class DominioException extends RuntimeException {

    protected DominioException(String mensaje) {
        super(mensaje);
    }
}
