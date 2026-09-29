/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comportamentales.state;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;

/**
 *
 * @author daferarte
 */
public class EstadoCancelada implements EstadoReserva {
    @Override
    public void confirmar(Reserva contexto) {
        throw new IllegalStateException("No se puede confirmar una reserva cancelada.");
    }

    @Override
    public void cancelar(Reserva contexto, int diasRestantes) {
        throw new IllegalStateException("La reserva ya está cancelada.");
    }
}
