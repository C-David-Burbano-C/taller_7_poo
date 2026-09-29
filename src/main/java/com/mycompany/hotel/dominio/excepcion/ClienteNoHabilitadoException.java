package com.mycompany.hotel.dominio.excepcion;

import java.util.UUID;

/** El cliente está suspendido y no puede operar. */
public class ClienteNoHabilitadoException extends ReglaNegocioException {

    public ClienteNoHabilitadoException(UUID clienteId) {
        super("El cliente " + clienteId + " está suspendido y no puede realizar reservas");
    }
}
