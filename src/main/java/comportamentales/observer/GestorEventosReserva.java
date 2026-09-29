/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comportamentales.observer;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author daferarte
 */
public class GestorEventosReserva {
    private final List<ReservaObserver> observadores = new ArrayList<>();

    public void suscribir(ReservaObserver observer) {
        if (observer != null && !observadores.contains(observer)) {
            observadores.add(observer);
        }
    }

    public void desuscribir(ReservaObserver observer) {
        observadores.remove(observer);
    }

    public void notificarCancelacion(Reserva reserva) {
        for (ReservaObserver obs : observadores) {
            obs.onReservaCancelada(reserva);
        }
    }

    public void notificarConfirmacion(Reserva reserva) {
        for (ReservaObserver obs : observadores) {
            obs.onReservaConfirmada(reserva);
        }
    }
}
