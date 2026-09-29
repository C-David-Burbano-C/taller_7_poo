package com.mycompany.hotel.dominio.modelo;

import com.mycompany.hotel.dominio.excepcion.ValidacionDominioException;
import java.util.Locale;
import java.util.regex.Pattern;

/** Value Object: correo electrónico normalizado (trim + minúsculas). */
public record Email(String valor) {

    private static final Pattern PATRON = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        valor = Requisitos.textoNoVacio(valor, "El email").toLowerCase(Locale.ROOT);
        if (!PATRON.matcher(valor).matches()) {
            throw new ValidacionDominioException("El formato del email no es válido: " + valor);
        }
    }
}
