package com.mycompany.hotel.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CrearClienteRequest(@NotBlank String nombre, @NotBlank String email) {
}
