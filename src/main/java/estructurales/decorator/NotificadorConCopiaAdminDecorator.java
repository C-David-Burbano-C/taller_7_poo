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
public class NotificadorConCopiaAdminDecorator extends NotificadorBaseDecorator {

    public NotificadorConCopiaAdminDecorator(NotificadorService wrapped) {
        super(wrapped);
    }

    @Override
    public void notificar(Cliente cliente, String mensaje) {
        super.notificar(cliente, mensaje);
        enviarCopiaOcultaAdmin(mensaje);
    }

    private void enviarCopiaOcultaAdmin(String mensaje) {
        System.out.println("BCC ADMIN Copia de seguridad enviada al administrador del sistema: \"" + mensaje + "\"");
    }
}
