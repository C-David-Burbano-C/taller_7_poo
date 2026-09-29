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
public class EstadoPendiente implements EstadoReserva {
    @Override
    public void confirmar(Reserva contexto) {
        contexto.getHabitacion().asignarAReserva();
        contexto.setEstado(new EstadoConfirmada());
    }

    @Override
    public void cancelar(Reserva contexto, int diasRestantes) {
        contexto.setEstado(new EstadoCancelada());
    }
}
