/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author daferarte
 */
public class ReservaArchivoRepository implements ReservaRepository {
    private final String rutaArchivo;

    public ReservaArchivoRepository(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public void guardar(Reserva reserva) {
        try (FileWriter fw = new FileWriter(this.rutaArchivo, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println("RESERVA_ID: " + reserva.getId() + 
                       " | CLIENTE: " + reserva.getCliente().getNombre() + 
                       " | HAB: " + reserva.getHabitacion().getNumero().valor() + 
                       " | ESTADO: " + reserva.getEstado());
            System.out.println("Registro guardado en " + this.rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al persistir en archivo: " + e.getMessage());
        }
    }
}
