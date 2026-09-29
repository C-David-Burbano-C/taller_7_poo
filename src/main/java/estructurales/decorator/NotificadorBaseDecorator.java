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
public abstract class NotificadorBaseDecorator implements NotificadorService {
    protected final NotificadorService envoltorio;

    public NotificadorBaseDecorator(NotificadorService envoltorio) {
        this.envoltorio = envoltorio;
    }

    @Override
    public void notificar(Cliente cliente, String mensaje) {
        envoltorio.notificar(cliente, mensaje);
    }
}
