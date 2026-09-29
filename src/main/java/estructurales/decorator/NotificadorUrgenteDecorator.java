/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructurales.decorator;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import notificacion.NotificadorService;

/**
 *
 * @author daferarte
 */
public class NotificadorUrgenteDecorator extends NotificadorBaseDecorator {

    public NotificadorUrgenteDecorator(NotificadorService wrapped) {
        super(wrapped);
    }

    @Override
    public void notificar(Cliente cliente, String mensaje) {
        String mensajeUrgente = "URGENTE / PRIORITARIO " + mensaje;
        super.notificar(cliente, mensajeUrgente);
    }
}
