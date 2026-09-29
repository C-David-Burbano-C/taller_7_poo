package com.mycompany.hotel.dominio.modelo;

import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import java.util.regex.Pattern;

/** Value Object: identificador visible de una habitación, formato {@code PXX-XXX} (ej. P05-501). */
public record NumeroHabitacion(String valor) {

    private static final Pattern PATRON = Pattern.compile("^P\\d{2}-\\d{3}$");

    public NumeroHabitacion {
        valor = Requisitos.textoNoVacio(valor, "El número de habitación").toUpperCase();
        if (!PATRON.matcher(valor).matches()) {
            throw new ValidacionDominioException(
                    "Formato inválido de número de habitación. Debe ser 'PXX-XXX' (ej. P03-302): " + valor);
        }
    }
}
