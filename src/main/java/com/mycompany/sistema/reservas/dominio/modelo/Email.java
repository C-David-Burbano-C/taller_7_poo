/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.mycompany.sistema.reservas.dominio.modelo;

/**
 *
 * @author daferarte
 */

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record Email(
    @Column(name = "email", nullable = false, unique = true, length = 150)
    String valor
    ) {
    private static final Pattern PATRON_EMAIL = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    public Email {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (!PATRON_EMAIL.matcher(valor).matches()) {
            throw new IllegalArgumentException("El formato del email no es válido: " + valor);
        }
    }
}
