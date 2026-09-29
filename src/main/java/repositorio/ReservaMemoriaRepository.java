/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author daferarte
 */
public class ReservaMemoriaRepository implements ReservaRepository {
    private final List<Reserva> tablaReservas = new ArrayList<>();

    @Override
    public void guardar(Reserva reserva) {
        tablaReservas.add(reserva);
        System.out.println("[Repositorio] Reserva " + reserva.getId() + " guardada en memoria. Total: " + tablaReservas.size());
    }
}
