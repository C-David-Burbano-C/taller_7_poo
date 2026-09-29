/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notificacion;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;

/**
 *
 * @author daferarte
 */
public class EmailNotificador implements NotificadorService {
    @Override
    public void notificar(Cliente cliente, String mensaje) {
        System.out.println("[EMAIL] Enviado a " + cliente.getNombre() + ": " + mensaje);
    }
}
