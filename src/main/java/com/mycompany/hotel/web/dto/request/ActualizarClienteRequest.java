package com.mycompany.hotel.web.dto.request;

/** PATCH: ambos campos son opcionales; {@code null} significa "no cambiar". */
public record ActualizarClienteRequest(String nombre, String email) {
}
