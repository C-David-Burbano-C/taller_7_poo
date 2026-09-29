package comportamentales.observer;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;

/**
 * Observador que envía alertas por correo electrónico ante cambios en la reserva.
 */
public class EmailObserver implements ReservaObserver {
    @Override
    public void onReservaCancelada(Reserva reserva) {
        System.out.println("[NOTIFICACIÓN OBSERVER] Correo enviado a " 
                + reserva.getCliente().getEmail().valor() 
                + ": Su reserva ha sido cancelada satisfactoriamente y la habitación ha sido liberada.");
    }

    @Override
    public void onReservaConfirmada(Reserva reserva) {
        System.out.println("[NOTIFICACIÓN OBSERVER] Correo enviado a " 
                + reserva.getCliente().getEmail().valor() 
                + ": Su reserva está 100% confirmada. ¡Esperamos su llegada!");
    }
}
