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
public class EstadoConfirmada implements EstadoReserva {
    @Override
    public void confirmar(Reserva contexto) {
        throw new IllegalStateException("La reserva ya se encuentra confirmada.");
    }

    @Override
    public void cancelar(Reserva contexto, int diasRestantes) {
        if (contexto.getEstrategiaCancelacion() != null) {
            double multa = contexto.getEstrategiaCancelacion().calcularMulta(contexto, diasRestantes);
            System.out.println("Multa procesada por sistema: $" + multa);
        }
        contexto.getHabitacion().habilitar();
        contexto.setEstado(new EstadoCancelada());

        if (contexto.getGestorEventos() != null) {
            contexto.getGestorEventos().notificarCancelacion(contexto);
        }
    }
}
