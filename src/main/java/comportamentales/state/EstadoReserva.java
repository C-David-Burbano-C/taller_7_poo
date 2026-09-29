/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comportamentales.state;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;

/**
 *
 * @author daferarte
 */
public interface EstadoReserva {
    void confirmar(Reserva contexto);
    void cancelar(Reserva contexto, int diasRestantes);
}
