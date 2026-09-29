/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructurales.decorator;

/**
 *
 * @author daferarte
 */
public class NotificadorBase implements NotificadorService {
    @Override public void notificar(String destinatario, String mensaje) {
        System.out.println("[EMAIL] Enviado a " + destinatario + ": " + mensaje);
    }
}
