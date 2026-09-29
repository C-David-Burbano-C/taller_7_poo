/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comportamentales.strategy;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;

/**
 *
 * @author daferarte
 */
public class CancelacionEstricta implements EstrategiaCancelacion {
    @Override
    public double calcularMulta(Reserva reserva, int diasRestantes) {
        if (diasRestantes < 7) return reserva.getTotal() * 0.50;
        return reserva.getTotal() * 0.10;
    }
}
