/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;

/**
 *
 * @author daferarte
 */
// DIP: La capa de dominio/servicio impone el contrato que la infraestructura debe cumplir
public interface ReservaRepository {
    void guardar(Reserva reserva);
}
