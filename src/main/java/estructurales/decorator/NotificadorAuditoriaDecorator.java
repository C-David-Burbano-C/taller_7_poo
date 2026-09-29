/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructurales.decorator;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import java.time.Instant;
import notificacion.NotificadorService;

/**
 *
 * @author daferarte
 */
public class NotificadorAuditoriaDecorator extends NotificadorBaseDecorator {
    
    public NotificadorAuditoriaDecorator(NotificadorService envoltorio) { 
        super(envoltorio); 
    }

    @Override
    public void notificar(Cliente cliente, String mensaje) {
        System.out.println("[AUDITORÍA UTC: " + Instant.now() + "] Iniciando notificación a: " + cliente.getNombre());
        super.notificar(cliente, mensaje);
    }
}
