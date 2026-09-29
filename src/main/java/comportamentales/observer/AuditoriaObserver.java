package comportamentales.observer;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import java.time.LocalDateTime;

/**
 * Observador que registra bitácora de auditoría cuando los eventos ocurren.
 */
public class AuditoriaObserver implements ReservaObserver {
    @Override
    public void onReservaCancelada(Reserva reserva) {
        System.out.println("[AUDITORÍA " + LocalDateTime.now() + "] Alerta: La reserva " 
                + reserva.getId() + " del cliente " + reserva.getCliente().getNombre() 
                + " ha sido CANCELADA.");
    }

    @Override
    public void onReservaConfirmada(Reserva reserva) {
        System.out.println("[AUDITORÍA " + LocalDateTime.now() + "] Registro: La reserva " 
                + reserva.getId() + " ha sido CONFIRMADA exitosamente.");
    }
}
