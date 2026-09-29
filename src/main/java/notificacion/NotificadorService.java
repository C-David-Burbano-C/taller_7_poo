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
// ISP: Contrato simple, sin métodos innecesarios para quien lo consuma
public interface NotificadorService {
    void notificar(Cliente cliente, String mensaje);
}
