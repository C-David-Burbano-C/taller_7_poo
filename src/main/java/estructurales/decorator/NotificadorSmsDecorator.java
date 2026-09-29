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
class NotificadorSmsDecorator extends NotificadorBaseDecorator {
    
    public NotificadorSmsDecorator(NotificadorService envoltorio) { 
        super(envoltorio); 
    }

    @Override
    public void notificar(Cliente cliente, String mensaje) {
        super.notificar(cliente, mensaje);
        System.out.println("[SMS] Copia enviada al teléfono de " + cliente.getNombre());
    }
}
