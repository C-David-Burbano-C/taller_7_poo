package com.mycompany.hotel.web.dto.response;

import java.util.UUID;

public record ClienteResponse(UUID id, String nombre, String email, boolean activo, int penalizaciones) {
}
