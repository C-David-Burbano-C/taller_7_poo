package com.mycompany.hotel.dominio.excepcion;

/** Un dato recibido viola un invariante (campo nulo, vacío, fuera de rango, formato inválido). */
public class ValidacionDominioException extends DominioException {

    public ValidacionDominioException(String mensaje) {
        super(mensaje);
    }
}
