package com.mycompany.hotel.dominio.modelo;

import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * Value Object: cantidad monetaria no negativa con moneda. Nunca usa double/float;
 * la escala se normaliza a 2 decimales para que equals() sea consistente.
 */
public record Dinero(BigDecimal cantidad, Currency moneda) {

    public static final Currency MONEDA_POR_DEFECTO = Currency.getInstance("USD");

    public Dinero {
        Requisitos.noNulo(cantidad, "La cantidad de dinero");
        Requisitos.noNulo(moneda, "La moneda");
        if (cantidad.signum() < 0) {
            throw new ValidacionDominioException("La cantidad de dinero no puede ser negativa: " + cantidad);
        }
        cantidad = cantidad.setScale(2, RoundingMode.HALF_UP);
    }

    public static Dinero de(BigDecimal cantidad) {
        return new Dinero(cantidad, MONEDA_POR_DEFECTO);
    }

    public static Dinero de(String cantidad) {
        Requisitos.textoNoVacio(cantidad, "La cantidad de dinero");
        return de(new BigDecimal(cantidad));
    }

    public static Dinero cero() {
        return de(BigDecimal.ZERO);
    }

    public Dinero sumar(Dinero otro) {
        exigirMismaMoneda(Requisitos.noNulo(otro, "El monto a sumar"));
        return new Dinero(cantidad.add(otro.cantidad), moneda);
    }

    public Dinero multiplicadoPor(long factor) {
        if (factor < 0) {
            throw new ValidacionDominioException("El factor no puede ser negativo: " + factor);
        }
        return new Dinero(cantidad.multiply(BigDecimal.valueOf(factor)), moneda);
    }

    /** @param porcentaje entero entre 0 y 100 */
    public Dinero porcentaje(int porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new ValidacionDominioException("El porcentaje debe estar entre 0 y 100: " + porcentaje);
        }
        return new Dinero(cantidad.multiply(BigDecimal.valueOf(porcentaje))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP), moneda);
    }

    public boolean esCero() {
        return cantidad.signum() == 0;
    }

    public boolean esMayorQueCero() {
        return cantidad.signum() > 0;
    }

    private void exigirMismaMoneda(Dinero otro) {
        if (!moneda.equals(otro.moneda)) {
            throw new ValidacionDominioException(
                    "No se pueden operar monedas distintas: " + moneda + " y " + otro.moneda);
        }
    }
}
